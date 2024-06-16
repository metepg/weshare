package com.weshare.service;

import com.weshare.model.Bill;
import com.weshare.model.SearchFilter;
import com.weshare.repository.BillRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BillService {
    private static final int TOTAL_CATEGORIES = 6;
    private final BillRepository billRepository;

    BillService(BillRepository billRepository){
        this.billRepository = billRepository;
    }

    public Bill create(Bill bill) {
        return billRepository.save(bill);
    }

    public List<Bill> findAllFromLastSixMonths() {
        LocalDate today = LocalDate.now();
        LocalDate sixMonthsAgo = today.minusMonths(6);
        return billRepository.findAllByDateBetween(sixMonthsAgo, today);
    }

    public List<Bill> findBillsByFilter(SearchFilter filter) {
        if (filter == null || hasNoFilters(filter)) {
            return findAllFromLastSixMonths();
        }
        String text = filter.description();
        List<Integer> categories = filter.categories();
        LocalDate startDate = null;
        LocalDate endDate = null;

        if (filter.range() != null && !filter.range().isEmpty()) {
            startDate = convertToLocalDate(filter.range().get(0));
            if (filter.range().size() == 2) {
                endDate = convertToLocalDate(filter.range().get(1));
            }
        }
        return billRepository.findByFilter(text, startDate, endDate, categories);
    }

    public List<Bill> payDebt() {
        billRepository.payDebt();
        return findAllFromLastSixMonths();
    }

    public double getTotalDebtByName(String name) {
        List<Bill> bills = billRepository.findAllUnpaidBills();
        return bills
                .stream()
                .mapToDouble(bill -> getUnpaidAmount(name, bill))
                .sum();
    }

    public List<Bill> findAllByYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return billRepository.findAllByDateBetween(startDate, endDate);
    }

    private double getUnpaidAmount(String name, Bill bill) {
        double total = bill.getAmount() - bill.getOwnAmount();
        return bill.getOwner().equals(name)
                ? total
                : -total;
    }

    private boolean hasNoFilters(SearchFilter searchFilter) {
       if (searchFilter == null) {
           return true;
       }
       return searchFilter.description().isBlank() && searchFilter.range().isEmpty() && searchFilter.categories().size() == TOTAL_CATEGORIES;
    }

    private LocalDate convertToLocalDate(Date date) {
        return Optional.ofNullable(date)
                .map(d -> d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .orElse(null);
    }
}
