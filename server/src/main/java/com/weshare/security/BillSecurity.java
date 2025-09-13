package com.weshare.security;

import com.weshare.repository.BillRepository;
import com.weshare.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("billSecurity")
@RequiredArgsConstructor
public class BillSecurity {

    private final BillRepository billRepository;
    private final SecurityService securityService;

    public boolean isOwner(Integer billId) {
        return billRepository.findById(billId)
            .map(bill -> bill.getOwner().getId().equals(securityService.getCurrentUser().id()))
            .orElse(false);
    }
}
