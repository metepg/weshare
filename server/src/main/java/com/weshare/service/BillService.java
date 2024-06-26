package com.weshare.service;

import com.weshare.model.Bill;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.model.User;
import com.weshare.repository.BillRepository;
import com.weshare.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BillService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final static Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final static Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "date");

    BillService(BillRepository billRepository, UserRepository userRepository){
        this.billRepository = billRepository;
        this.userRepository = userRepository;
    }

    public Bill create(Bill bill) {
        User user = userRepository.findByName(bill.getOwner().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        bill.setGroup(user.getGroup());
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
        Optional<List<User>> users = userRepository.findUsersByNameIn(filter.users());

        if (users.isEmpty() || categories.isEmpty()) {
            return List.of();
        }

        return billRepository.findByFilter(description, categories, users.get(), SORT_BY_DATE);
    }

    public List<Bill> payDebt() {
        billRepository.payDebt();
        return findAllFromLastSixMonths();
    }

    public double getTotalDebtByName(String name) {
        Optional<User> user = userRepository.findByName(name);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + name);
        }

        return billRepository.findBillsByGroupAndPaidIsFalse(user.get().getGroup())
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

        // TODO: Range filter
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.now().minusYears(10);
        return billRepository.findAllByDateBetween(startDate, endDate, SORT_BY_ID);
    }

    public Bill editBill(Bill bill) {
        User user = userRepository.findByName(bill.getOwner().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        bill.setGroup(user.getGroup());
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
        Optional<User> user = userRepository.findByName(name);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + name);
        }

        return billRepository.findBillsByOwner(user.get());
    }


    private double getUnpaidAmount(String name, Bill bill) {
        double total = bill.getAmount() - bill.getOwnAmount();
        return bill.getOwner().getName().equals(name)
                ? total
                : -total;
    }

}
