package com.weshare.service;

import com.weshare.dto.BillDTO;
import com.weshare.dto.BillEvent;
import com.weshare.dto.UserDTO;
import com.weshare.model.Bill;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.model.User;
import com.weshare.repository.BillRepository;
import com.weshare.util.BillConverter;
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

import static com.weshare.model.BillEventType.ADDED;
import static com.weshare.model.BillEventType.DEBT_PAID;
import static com.weshare.model.BillEventType.DELETED;
import static com.weshare.model.BillEventType.UPDATED;

@Service
@Transactional
@RequiredArgsConstructor
public class BillService {
    private static final Logger LOG = LoggerFactory.getLogger(BillService.class);
    private final BillRepository billRepository;
    private final BillConverter billConverter;
    private final UserService userService;
    private final SecurityService securityService;
    private final BillEventService billEventService;
    private static final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "date");

    @CacheEvict(value = {"recentBills", "billsByYear"}, allEntries = true)
    public BillDTO save(BillDTO dto) {
        Bill bill = billRepository.save(billConverter.dtoToBill(dto));
        BillDTO saved = billConverter.billToDTO(bill);
        LOG.debug("User '{}' saved bill: {}", securityService.getCurrentUser().name(), bill);
        billEventService.publish(new BillEvent(dto.id() == null ? ADDED : UPDATED, saved));
        return saved;
    }

    @Cacheable("recentBills")
    public List<BillDTO> findRecentBills() {
        LOG.debug("Fetching recent bills for user '{}'", securityService.getCurrentUser().name());
        Pageable pageable = PageRequest.of(0, 100);
        List<Bill> recentBills = billRepository.findRecentBills(securityService.getCurrentUser().groupId(), pageable);
        Collections.reverse(recentBills);
        return recentBills.stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public List<BillDTO> findBillsByFilter(SearchFilter filter) {
        if (filter == null) {
            return List.of();
        }
        LOG.debug("User '{}' finding bills with filter: {}", securityService.getCurrentUser().name(), filter);
        List<User> users = userService.findUsersByNameIn(filter.users());
        return billRepository.findByFilter(filter.description(), filter.categories(), users, SORT_BY_DATE)
                .stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    @CacheEvict(value = {"recentBills", "billsByYear"}, allEntries = true)
    public List<BillDTO> payDebt(BillDTO bill) {
        UserDTO currentUser = securityService.getCurrentUser();
        Bill saved = billRepository.save(billConverter.dtoToBill(bill));
        billRepository.payDebt();
        LOG.info("User '{}' paid debt", currentUser.name());
        billEventService.publish(new BillEvent(DEBT_PAID, null));
        return List.of(billConverter.billToDTO(saved));
    }

    @Cacheable(value = "billsByYear", key = "#year")
    public List<BillDTO> findAllByYear(Integer year) {
        LOG.debug("Fetching all bills for year {}", year);
        return billRepository.findAllByYear(year)
                .stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public List<BillDTO> getStats(StatsFilter filter) {
        if (filter == null || filter.range().isEmpty() || filter.username().isBlank()) {
            return List.of();
        }
        LOG.debug("User '{}' searching stats with filter: {}", securityService.getCurrentUser().name(), filter);
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.now().minusYears(10);
        return billRepository.findAllByDateBetween(startDate, endDate, SORT_BY_DATE)
                .stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    @CacheEvict(value = {"recentBills", "billsByYear"}, allEntries = true)
    public boolean deleteBillById(Integer id) {
        LOG.info("User '{}' deleting bill with ID: {}", securityService.getCurrentUser().name(), id);
        try {
            billRepository.deleteById(id);
            billEventService.publish(new BillEvent(
                DELETED, new BillDTO(id, 0, 0, null, null, false, null, null, null)
            ));
            return true;
        } catch (Exception e) {
            LOG.error("User '{}' failed to delete bill with ID: {}", securityService.getCurrentUser().name(), id, e);
            return false;
        }
    }

    public List<BillDTO> findBillsByUserId(Integer id) {
        LOG.debug("User '{}' fetching bills for user ID: {}", securityService.getCurrentUser().name(), id);
        User user = userService.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found when fetching bills for user ID: " + id));
        return billRepository.findBillsByOwner(user)
                .stream()
                .map(billConverter::billToDTO)
                .toList();
    }

    public double findUserDebtByUserId(Integer userId) {
        String username = securityService.getCurrentUser().name();
        LOG.debug("User '{}' retrieving debt for user ID: {}", username, userId);
        return billRepository.findUserDebtByUserId(userId);
    }
}