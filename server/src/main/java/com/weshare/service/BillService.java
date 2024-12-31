package com.weshare.service;

import com.weshare.dto.BillDTO;
import com.weshare.model.Bill;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.model.User;
import com.weshare.repository.BillRepository;
import com.weshare.repository.UserRepository;
import com.weshare.util.BillConverter;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BillService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final BillConverter billConverter;
    private final static Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final static Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "date");

    BillService(BillRepository billRepository, UserRepository userRepository, BillConverter billConverter){
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.billConverter = billConverter;
    }

    public BillDTO save(BillDTO dto) {
        Bill bill = billRepository.save(billConverter.dtoToBill(dto));
        return billConverter.billToDTO(bill);
    }

    public List<BillDTO> findAllFromLastSixMonths() {
        LocalDate today = LocalDate.now();
        LocalDate sixMonthsAgo = today.minusMonths(6);
        return billRepository.findAllByDateBetween(sixMonthsAgo, today, SORT_BY_ID).stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public List<BillDTO> findBillsByFilter(SearchFilter filter) {
        if (filter == null) {
            return List.of();
        }

        String description = filter.description();
        List<Integer> categories = filter.categories();

        List<User> users = userRepository.findUsersByNameIn(filter.users());

        return billRepository.findByFilter(description, categories, users, SORT_BY_DATE).stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public List<BillDTO> payDebt() {
        billRepository.payDebt();
        return findAllFromLastSixMonths();
    }

    public List<BillDTO> findAllByYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return billRepository.findAllByDateBetween(startDate, endDate, SORT_BY_ID)
                .stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public List<BillDTO> getStats(StatsFilter filter) {
        if (filter == null) return List.of();
        if (filter.range().isEmpty()) return List.of();
        if (filter.username().isBlank()) return List.of();

        // TODO: Range filter
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.now().minusYears(10);
        return billRepository.findAllByDateBetween(startDate, endDate, SORT_BY_ID).stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public boolean deleteBillById(Integer id) {
        try {
            billRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public List<BillDTO> findBillsByUserId(Integer id) {
        User user = userRepository.findUserById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return billRepository.findBillsByOwner(user).stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public double findUserDebtByUserId(Integer userId) {
        return billRepository.findUserDebtByUserId(userId);
    }

}
