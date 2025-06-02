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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@RequiredArgsConstructor
public class BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);
    private final BillRepository billRepository;
    private final BillConverter billConverter;
    private static final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "date");
    private final UserService userService;

    @CacheEvict(value = {"recentBills", "billsByYear"}, allEntries = true)
    public BillDTO save(BillDTO dto) {
        Bill bill = billRepository.save(billConverter.dtoToBill(dto));
        logger.debug("User '{}' saved bill: {}", SecurityUtil.getCurrentUser().name(), bill);
        return billConverter.billToDTO(bill);
    }

    @Cacheable("recentBills")
    public List<BillDTO> findRecentBills() {
        logger.debug("Fetching recent bills for user '{}'", SecurityUtil.getCurrentUser().name());
        Pageable pageable = PageRequest.of(0, 100);
        List<Bill> recentBills = billRepository.findRecentBills(SecurityUtil.getCurrentUser().groupId(), pageable);
        Collections.reverse(recentBills);
        return recentBills.stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public List<BillDTO> findBillsByFilter(SearchFilter filter) {
        if (filter == null) {
            return List.of();
        }
        logger.debug("User '{}' finding bills with filter: {}", SecurityUtil.getCurrentUser().name(), filter);
        List<User> users = userService.findUsersByNameIn(filter.users());
        return billRepository.findByFilter(filter.description(), filter.categories(), users, SORT_BY_DATE)
                .stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    @CacheEvict(value = {"recentBills", "billsByYear"}, allEntries = true)
    public List<BillDTO> payDebt(BillDTO bill) {
        UserDTO currentUser = SecurityUtil.getCurrentUser();
        save(bill);
        billRepository.payDebt();
        logger.info("User '{}' paid debt", currentUser.name());
        List<Bill> recentBills = billRepository.findRecentBills(currentUser.groupId(), PageRequest.of(0, 100));
        Collections.reverse(recentBills);
        return recentBills.stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    @Cacheable(value = "billsByYear", key = "#year")
    public List<BillDTO> findAllByYear(Integer year) {
        logger.debug("Fetching all bills for year {}", year);
        return billRepository.findAllByYear(year)
                .stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public List<BillDTO> getStats(StatsFilter filter) {
        if (filter == null || filter.range().isEmpty() || filter.username().isBlank()) {
            return List.of();
        }
        logger.debug("User '{}' searching stats with filter: {}", SecurityUtil.getCurrentUser().name(), filter);
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.now().minusYears(10);
        return billRepository.findAllByDateBetween(startDate, endDate, SORT_BY_DATE)
                .stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    @CacheEvict(value = {"recentBills", "billsByYear"}, allEntries = true)
    public boolean deleteBillById(Integer id) {
        logger.info("User '{}' deleting bill with ID: {}", SecurityUtil.getCurrentUser().name(), id);
        try {
            billRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("User '{}' failed to delete bill with ID: {}", SecurityUtil.getCurrentUser().name(), id, e);
            return false;
        }
    }

    public List<BillDTO> findBillsByUserId(Integer id) {
        logger.debug("User '{}' fetching bills for user ID: {}", SecurityUtil.getCurrentUser().name(), id);
        User user = userService.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return billRepository.findBillsByOwner(user)
                .stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public double findUserDebtByUserId(Integer userId) {
        String username = SecurityUtil.getCurrentUser().name();
        logger.debug("User '{}' retrieving debt for user ID: {}", username, userId);
        return billRepository.findUserDebtByUserId(userId);
    }
}