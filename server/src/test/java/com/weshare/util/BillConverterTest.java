package com.weshare.util;

import com.weshare.dto.BillDTO;
import com.weshare.model.Bill;
import com.weshare.model.Category;
import com.weshare.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillConverterTest {

    @InjectMocks
    private BillConverter converter;

    @Mock
    private EntityManager entityManager;

    @Test
    void testBillToDTO() {
        User owner = new User();
        owner.setId(1);
        owner.setName("Test User");

        Category category = new Category();
        category.setId(2);

        Bill bill = Bill.builder()
            .id(10)
            .amount(100.0)
            .ownAmount(50.0)
            .description("Test")
            .date(LocalDate.now())
            .paid(true)
            .owner(owner)
            .category(category)
            .build();

        BillDTO dto = converter.billToDTO(bill);

        assertEquals(10, dto.id());
        assertEquals(100.0, dto.amount());
        assertEquals(50.0, dto.ownAmount());
        assertEquals("Test", dto.description());
        assertEquals(bill.getDate(), dto.date());
        assertTrue(dto.paid());
        assertEquals(2, dto.categoryId());
        assertEquals(1, dto.ownerId());
        assertEquals("Test User", dto.ownerName());
    }

    @Test
    void testDtoToBill() {
        BillDTO dto = new BillDTO(10, 100.0, 50.0, "Test", LocalDate.now(), true, 22, 11, "Test User");

        User user = new User();
        user.setId(11);

        Category category = new Category();
        category.setId(22);

        when(entityManager.getReference(User.class, 11)).thenReturn(user);
        when(entityManager.getReference(Category.class, 22)).thenReturn(category);

        Bill bill = converter.dtoToBill(dto);

        assertEquals(10, bill.getId());
        assertEquals(100.0, bill.getAmount());
        assertEquals(50.0, bill.getOwnAmount());
        assertEquals("Test", bill.getDescription());
        assertEquals(dto.date(), bill.getDate());
        assertTrue(bill.isPaid());
        assertEquals(user, bill.getOwner());
        assertEquals(category, bill.getCategory());
    }
}
