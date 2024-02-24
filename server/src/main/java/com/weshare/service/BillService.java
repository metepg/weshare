package com.weshare.service;

import com.weshare.model.Bill;
import com.weshare.repository.BillRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillService {
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
        return billRepository.findAllFromLastSixMonths(sixMonthsAgo, today);
    }

    public List<Bill> payDebt() {
        billRepository.payDebt();
        return findAllFromLastSixMonths();
    }

    public double getTotalDebtByName(String name) {
        List<Bill> allUnpaidBills = billRepository.findAllUnpaidBills();

        // Partition bills by ownership
        Map<Boolean, List<Bill>> partitionedBills = allUnpaidBills.stream()
                .collect(Collectors.partitioningBy(bill -> name.equals(bill.getOwner())));

        double sumOwned = partitionedBills.get(true).stream()
                .mapToDouble(bill -> bill.getAmount() - bill.getOwnAmount())
                .sum();
        double sumNotOwned = partitionedBills.get(false).stream()
                .mapToDouble(bill -> bill.getAmount() - bill.getOwnAmount())
                .sum();

        return sumNotOwned - sumOwned;
    }

    public List<Bill> findAllByYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return billRepository.findAllByDateBetween(startDate, endDate);
    }
}
