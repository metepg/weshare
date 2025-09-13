package com.weshare.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.weshare.dto.UserDTO;
import com.weshare.mocks.MockDataProvider;
import com.weshare.model.Bill;
import com.weshare.model.Category;
import com.weshare.model.Group;
import com.weshare.model.User;
import com.weshare.repository.BillRepository;
import com.weshare.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class BillSecurityTest {

    @Mock
    BillRepository billRepository;

    @Mock
    SecurityService securityService;

    @InjectMocks
    BillSecurity billSecurity;

    @Test
    void returnsTrueWhenCurrentUserIsOwner() {
        Group group = MockDataProvider.createMockGroup();
        User owner = MockDataProvider.createMockUser(group);
        owner.setId(1);

        Category category = MockDataProvider.createMockCategory(group);
        Bill bill = MockDataProvider.createMockBill(owner, category);
        bill.setId(10);

        when(billRepository.findById(10)).thenReturn(Optional.of(bill));
        when(securityService.getCurrentUser())
            .thenReturn(new UserDTO(owner.getId(), owner.getName(), group.getId(), owner.getRole()));

        boolean result = billSecurity.isOwner(10);

        assertThat(result).isTrue();
    }

    @Test
    void returnsFalseWhenCurrentUserIsNotOwner() {
        Group group = MockDataProvider.createMockGroup();
        User owner = MockDataProvider.createMockUser(group);
        owner.setId(1);

        Category category = MockDataProvider.createMockCategory(group);
        Bill bill = MockDataProvider.createMockBill(owner, category);
        bill.setId(20);

        when(billRepository.findById(20)).thenReturn(Optional.of(bill));
        when(securityService.getCurrentUser())
            .thenReturn(new UserDTO(99, "otherUser", group.getId(), "ROLE_USER"));

        boolean result = billSecurity.isOwner(20);

        assertThat(result).isFalse();
    }

    @Test
    void returnsFalseWhenBillNotFound() {
        when(billRepository.findById(30)).thenReturn(Optional.empty());

        boolean result = billSecurity.isOwner(30);

        assertThat(result).isFalse();
    }
}
