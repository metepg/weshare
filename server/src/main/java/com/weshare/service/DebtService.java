package com.weshare.service;

import com.weshare.repository.BillRepository;
import org.springframework.stereotype.Service;

@Service
public class DebtService {

    private final BillRepository billRepository;

    DebtService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Double calculateTotalDebt(Integer userId) {
        return billRepository.findUserDebtByUserId(userId);
    }

}
