package com.weshare.controller;

import com.weshare.dto.BillDTO;
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
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO bill) {
        return new ResponseEntity<>(billService.save(bill), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("")
    public List<BillDTO> findBillsFromLastSixMonths() {
        return billService.findAllFromLastSixMonths();
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/{id}")
    public List<BillDTO> getBillsByUserId(@PathVariable Integer id) {
        return billService.getBillsByUserId(id);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @PostMapping("/search")
    public List<BillDTO> findBillsByFilter(@RequestBody SearchFilter filter) {
        return billService.findBillsByFilter(filter);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/statistics/{year}")
    public List<BillDTO> findBills(@PathVariable Integer year) {
        return billService.findAllByYear(year);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @PostMapping("/pay")
    public ResponseEntity<List<BillDTO>> payDebt(@RequestBody BillDTO bill) {
        // Bill created here is used in UI to indicate all bills are paid
        createBill(bill);
        return new ResponseEntity<>(billService.payDebt(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/stats")
    public List<BillDTO> getStats(@RequestBody StatsFilter filter) {
        return billService.getStats(filter);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @PutMapping()
    public BillDTO editBill(@RequestBody BillDTO bill) {
        return billService.save(bill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBill(@PathVariable Integer id) {
        return ResponseEntity.ok(billService.deleteBillById(id));
    }
}
