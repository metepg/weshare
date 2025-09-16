package com.weshare.controller;

import com.weshare.dto.BillDTO;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.service.BillService;
import com.weshare.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private static final Logger LOG = LoggerFactory.getLogger(BillController.class);
    private final BillService billService;
    private final SecurityService securityService;

    @PostMapping()
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2)")
    @ResponseStatus(HttpStatus.CREATED)
    public BillDTO createBill(@RequestBody BillDTO bill) {
        LOG.info("User '{}' is saving bill: {}", securityService.getCurrentUser().name(), bill);
        return billService.save(bill);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2)")
    public List<BillDTO> findRecentBills() {
        return billService.findRecentBills();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2) and @securityService.isUserInSameGroup(#userId)")
    public List<BillDTO> findBillsByUserId(@PathVariable Integer userId) {
        return billService.findBillsByUserId(userId);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2)")
    public List<BillDTO> findBillsByFilter(@RequestBody SearchFilter filter) {
        return billService.findBillsByFilter(filter);
    }

    @GetMapping("/statistics/{year}")
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2)")
    public List<BillDTO> findBills(@PathVariable Integer year) {
        return billService.findAllByYear(year);
    }

    @PostMapping("/pay")
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2)")
    @ResponseStatus(HttpStatus.OK)
    public List<BillDTO> payDebt(@RequestBody BillDTO bill) {
        return billService.payDebt(bill);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2)")
    public List<BillDTO> getStats(@RequestBody StatsFilter filter) {
        return billService.getStats(filter);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2) and @billSecurity.isOwner(#bill.id())")
    public BillDTO editBill(@RequestBody BillDTO bill) {
        LOG.info("User '{}' is editing bill: {}", securityService.getCurrentUser().name(), bill);
        return billService.save(bill);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2) and @billSecurity.isOwner(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean deleteBill(@PathVariable Integer id) {
        return billService.deleteBillById(id);
    }
}