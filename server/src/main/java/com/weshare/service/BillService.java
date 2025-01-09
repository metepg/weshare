package com.weshare.service;

import com.weshare.dto.BillDTO;
import com.weshare.dto.UserDTO;
import com.weshare.model.Bill;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.model.User;
import com.weshare.repository.BillRepository;
import com.weshare.util.BillConverter;
import com.weshare.util.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class BillService {
    private final BillRepository billRepository;
    private final BillConverter billConverter;
    private final static Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "date");
    private final UserService userService;

    BillService(BillRepository billRepository, BillConverter billConverter, UserService userService) {
        this.billRepository = billRepository;
        this.billConverter = billConverter;
        this.userService = userService;
    }

    public BillDTO save(BillDTO dto) {
        Bill bill = billRepository.save(billConverter.dtoToBill(dto));
        return billConverter.billToDTO(bill);
    }

    public List<BillDTO> findRecentBills() {
        UserDTO currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) return List.of();

        Pageable pageable = PageRequest.of(0, 100);
        List<Bill> recentBills = billRepository.findRecentBills(currentUser.groupId(), pageable);
        Collections.reverse(recentBills);

        return recentBills.stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public List<BillDTO> findBillsByFilter(SearchFilter filter) {
        if (filter == null) {
            return List.of();
        }

        String description = filter.description();
        List<Integer> categories = filter.categories();
        List<User> users = userService.findUsersByNameIn(filter.users());

        return billRepository.findByFilter(description, categories, users, SORT_BY_DATE).stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public List<BillDTO> payDebt(BillDTO bill) {
        // Bill created here is used in UI to indicate all bills are paid
        this.save(bill);
        billRepository.payDebt();
        return findRecentBills();
    }

    public List<BillDTO> findAllByYear(Integer year) {
        return billRepository.findAllByYear(year)
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
        return billRepository.findAllByDateBetween(startDate, endDate, SORT_BY_DATE).stream()
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
        User user = userService.findUserById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return billRepository.findBillsByOwner(user).stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public double findUserDebtByUserId(Integer userId) {
        return billRepository.findUserDebtByUserId(userId);
    }

}
