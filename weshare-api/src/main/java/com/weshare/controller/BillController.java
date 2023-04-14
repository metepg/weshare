package com.weshare.controller;

import com.weshare.model.Bill;
import com.weshare.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BillController {

    @Autowired
    BillService billService;

    /**
     * Create new Bill
     */
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @PostMapping("/bills/create")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        return new ResponseEntity<>(billService.create(bill), HttpStatus.CREATED);
    }

    /**
     * Find bills for main view (last 6 months)
     * TODO: make it scrollable by month
     */
    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/bills")
    public List<Bill> findBillsFromlastSixMonths() {
        return billService.findAllFromLastSixMonths();
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/bills/statistics/{year}")
    public List<Bill> findBills(@PathVariable Integer year) {
        return billService.findAllByYear(year);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @PostMapping("/bills/pay")
    public ResponseEntity<List<Bill>> payDebt(@RequestBody Bill bill) {
        createBill(bill);
        return new ResponseEntity<>(billService.payDebt(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/bills/total")
    public double getTotalAmount(Principal auth) {
        String currentUser = auth.getName();
        return billService.getTotalDebtByName(currentUser);
    }

}
