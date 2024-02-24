package com.weshare.controller;

import com.weshare.model.Bill;
import com.weshare.service.BillService;
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
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    BillController(BillService billService) {
       this.billService = billService;
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @PostMapping("/create")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        return new ResponseEntity<>(billService.create(bill), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("")
    public List<Bill> findBillsFromLastSixMonths() {
        return billService.findAllFromLastSixMonths();
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/statistics/{year}")
    public List<Bill> findBills(@PathVariable Integer year) {
        return billService.findAllByYear(year);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @PostMapping("/pay")
    public ResponseEntity<List<Bill>> payDebt(@RequestBody Bill bill) {
        // Bill created here is used in UI to indicate all bills are paid
        createBill(bill);
        return new ResponseEntity<>(billService.payDebt(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/total")
    public double getTotalAmount(Principal auth) {
        String currentUser = auth.getName();
        return billService.getTotalDebtByName(currentUser);
    }

}
