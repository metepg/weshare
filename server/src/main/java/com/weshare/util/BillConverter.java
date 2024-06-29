package com.weshare.util;

import com.weshare.dto.BillDTO;
import com.weshare.model.Bill;
import com.weshare.model.Category;
import com.weshare.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class BillConverter {
    @PersistenceContext
    private EntityManager entityManager;

    public BillDTO billToDTO(Bill bill) {
        return new BillDTO(
                bill.getId(),
                bill.getAmount(),
                bill.getOwnAmount(),
                bill.getDescription(),
                bill.getDate(),
                bill.isPaid(),
                bill.getCategory().getId(),
                bill.getOwner().getId(),
                bill.getOwner().getName()
        );
    }

    public Bill dtoToBill(BillDTO dto) {
        Bill bill = new Bill();

        if (dto.id() != null) {
            bill.setId(dto.id());
        }

        bill.setAmount(dto.amount());
        bill.setOwnAmount(dto.ownAmount());
        bill.setDescription(dto.description());
        bill.setPaid(dto.paid());
        bill.setDate(dto.date());
        bill.setOwner(entityManager.getReference(User.class, dto.ownerId()));
        bill.setCategory(entityManager.getReference(Category.class, dto.categoryId()));
        return bill;
    }
}
