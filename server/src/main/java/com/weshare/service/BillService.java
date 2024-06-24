package com.weshare.service;

import com.weshare.model.Bill;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.repository.BillRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class BillService {
    private final BillRepository billRepository;
    private final static Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");

    BillService(BillRepository billRepository){
        this.billRepository = billRepository;
    }

    public Bill create(Bill bill) {
        return billRepository.save(bill);
    }

    public List<Bill> findAllFromLastSixMonths() {
        LocalDate today = LocalDate.now();
        LocalDate sixMonthsAgo = today.minusMonths(6);
        return billRepository.findAllByDateBetween(sixMonthsAgo, today, SORT_BY_ID);
    }

    public List<Bill> findBillsByFilter(SearchFilter filter) {
        if (filter == null) {
            return List.of();
        }
        String description = filter.description();
        List<Integer> categories = filter.categories();
        List<String> users = filter.users();

        return billRepository.findByFilter(description, categories, users);
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
        return billRepository.findAllByDateBetween(startDate, endDate, SORT_BY_ID);
    }

    public List<Bill> getStats(StatsFilter filter) {
        if (filter == null) return List.of();
        if (filter.range().isEmpty()) return List.of();
        if (filter.username().isBlank()) return List.of();
        List<String> range = filter.range();

        LocalDate endDate = range.size() == 1
                ? LocalDate.now()
                : convertToLocalDate(range.get(0));
        LocalDate startDate = range.size() == 1
                ? LocalDate.now().minusYears(10)
                : convertToLocalDate(range.get(1));
        return billRepository.findAllByDateBetween(startDate, endDate, SORT_BY_ID);
    }


    private double getUnpaidAmount(String name, Bill bill) {
        double total = bill.getAmount() - bill.getOwnAmount();
        return bill.getOwner().equals(name)
                ? total
                : -total;
    }

    private static LocalDate convertToLocalDate(String isoDate) {
        ZonedDateTime zdt = ZonedDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME);
        return zdt.toLocalDate();
    }

    public Bill editBill(Bill bill) {
        return billRepository.save(bill);
    }

    public boolean deleteBillById(Long id) {
        try {
            billRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public List<Bill> getBillsByUserName(String name) {
        return billRepository.findByOwnerAndCategory(name);
    }

}
