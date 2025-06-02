package com.weshare.controller;

import com.weshare.dto.BillDTO;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.service.BillService;
import com.weshare.util.SecurityUtil;
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

    private static final Logger logger = LoggerFactory.getLogger(BillController.class);
    private final BillService billService;

    @PostMapping()
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @ResponseStatus(HttpStatus.CREATED)
    public BillDTO createBill(@RequestBody BillDTO bill) {
        logger.info("User '{}' is saving bill: {}", SecurityUtil.getCurrentUser().name(), bill);
        return billService.save(bill);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    public List<BillDTO> findRecentBills() {
        return billService.findRecentBills();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    public List<BillDTO> findBillsByUserId(@PathVariable Integer id) {
        return billService.findBillsByUserId(id);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    public List<BillDTO> findBillsByFilter(@RequestBody SearchFilter filter) {
        return billService.findBillsByFilter(filter);
    }

    @GetMapping("/statistics/{year}")
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    public List<BillDTO> findBills(@PathVariable Integer year) {
        return billService.findAllByYear(year);
    }

    @PostMapping("/pay")
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @ResponseStatus(HttpStatus.OK)
    public List<BillDTO> payDebt(@RequestBody BillDTO bill) {
        return billService.payDebt(bill);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    public List<BillDTO> getStats(@RequestBody StatsFilter filter) {
        return billService.getStats(filter);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    public BillDTO editBill(@RequestBody BillDTO bill) {
        logger.info("User '{}' is editing bill: {}", SecurityUtil.getCurrentUser().name(), bill);
        return billService.save(bill);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean deleteBill(@PathVariable Integer id) {
        return billService.deleteBillById(id);
    }
}
