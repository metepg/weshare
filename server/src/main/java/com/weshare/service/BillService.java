package com.weshare.service;

import com.weshare.model.Bill;
import com.weshare.repository.BillRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        List<Bill> bills = billRepository.findAllUnpaidBills();
        return bills
                .stream()
                .mapToDouble(bill -> getOwnAmount(bill, name))
                .sum();
    }

    public List<Bill> findAllByYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return billRepository.findAllByDateBetween(startDate, endDate);
    }

    private double getOwnAmount(Bill bill, String name) {
        double total = bill.getAmount() - bill.getOwnAmount();
        return bill.getOwner().equals(name)
                ? total
                : -total;
    }
}
