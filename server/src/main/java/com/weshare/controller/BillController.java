package com.weshare.controller;

import com.weshare.model.Bill;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @PostMapping("")
    public List<Bill> findBillsByFilter(@RequestBody SearchFilter filter) {
        return billService.findBillsByFilter(filter);
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
    @GetMapping("/debt")
    public double getTotalDebtAmount(Principal auth) {
        String currentUser = auth.getName();
        return billService.getTotalDebtByName(currentUser);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/stats")
    public List<Bill> getStats(@RequestBody StatsFilter filter) {
        return billService.getStats(filter);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @PutMapping()
    public Bill editBill(@RequestBody Bill bill) {
        return billService.editBill(bill);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/total/{name}")
    public List<Bill> getBillsByUserName(@PathVariable String name) {
        return billService.getBillsByUserName(name);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBill(@PathVariable Long id) {
        return ResponseEntity.ok(billService.deleteBillById(id));
    }
}
