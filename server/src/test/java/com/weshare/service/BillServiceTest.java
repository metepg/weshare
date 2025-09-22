package com.weshare.service;

import com.weshare.dto.BillDTO;
import com.weshare.dto.UserDTO;
import com.weshare.mocks.MockDataProvider;
import com.weshare.model.Bill;
import com.weshare.model.Category;
import com.weshare.model.Group;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.model.User;
import com.weshare.repository.BillRepository;
import com.weshare.util.BillConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private BillRepository billRepository;
    @Mock
    private UserService userService;
    @Mock
    private SecurityService securityService;
    @Mock
    private BillConverter billConverter;
    @SuppressWarnings("unused")
    @Mock
    private BillEventService billEventService;

    @InjectMocks
    private BillService billService;

    private Group mockGroup;
    private User mockUserEntity;
    private UserDTO mockUserDto;
    private Category mockCategory;

    @BeforeEach
    void setup() {
        mockGroup = MockDataProvider.createMockGroup();
        mockUserEntity = MockDataProvider.createMockUser(mockGroup);
        mockUserDto = new UserDTO(
            mockUserEntity.getId(),
            mockUserEntity.getName(),
            mockGroup.getId(),
            mockUserEntity.getRole()
        );
        lenient().when(securityService.getCurrentUser()).thenReturn(mockUserDto);
        mockCategory = MockDataProvider.createMockCategory(mockGroup);
        mockCategory.setId(98);
    }

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("saves bill and returns dto")
        void saveReturnsDto() {
            BillDTO inputDto = MockDataProvider.createMockBillDTO(mockUserEntity, mockCategory);
            Bill toSave = MockDataProvider.createMockBill(mockUserEntity, mockCategory);
            when(billConverter.dtoToBill(inputDto)).thenReturn(toSave);

            Bill saved = MockDataProvider.createMockBill(mockUserEntity, mockCategory);
            saved.setId(1);
            when(billRepository.save(toSave)).thenReturn(saved);

            BillDTO savedDto = new BillDTO(
                saved.getId(), saved.getAmount(), saved.getOwnAmount(),
                saved.getDescription(), saved.getDate(), saved.isPaid(),
                saved.getCategory().getId(), saved.getOwner().getId(),
                saved.getOwner().getName()
            );
            when(billConverter.billToDTO(saved)).thenReturn(savedDto);

            BillDTO result = billService.save(inputDto);

            assertThat(result.id()).isEqualTo(1);
            assertThat(result.ownerId()).isEqualTo(mockUserEntity.getId());
        }

    }

    @Nested
    @DisplayName("findRecentBills()")
    class FindRecentBillsTests {

        @Test
        @DisplayName("returns recent bills in reverse order")
        void shouldReturnRecentBillsInReverseOrder() {
            Bill older = MockDataProvider.createMockBill(mockUserEntity, mockCategory);
            older.setId(2);
            older.setDate(LocalDate.now().minusDays(1));

            Bill newer = MockDataProvider.createMockBill(mockUserEntity, mockCategory);
            newer.setId(3);
            newer.setDate(LocalDate.now());

            when(billRepository.findRecentBills(
                eq(mockUserDto.groupId()),
                any(Pageable.class))
            ).thenReturn(new ArrayList<>(List.of(newer, older)));

            BillDTO olderDto = new BillDTO(
                older.getId(),
                older.getAmount(),
                older.getOwnAmount(),
                older.getDescription(),
                older.getDate(),
                older.isPaid(),
                older.getCategory().getId(),
                older.getOwner().getId(),
                older.getOwner().getName()
            );
            BillDTO newerDto = new BillDTO(
                newer.getId(),
                newer.getAmount(),
                newer.getOwnAmount(),
                newer.getDescription(),
                newer.getDate(),
                newer.isPaid(),
                newer.getCategory().getId(),
                newer.getOwner().getId(),
                newer.getOwner().getName()
            );
            when(billConverter.billToDTO(newer)).thenReturn(newerDto);
            when(billConverter.billToDTO(older)).thenReturn(olderDto);

            List<BillDTO> result = billService.findRecentBills();

            assertThat(result).extracting(BillDTO::id).containsExactly(older.getId(), newer.getId());
        }

    }

    @Nested
    @DisplayName("findBillsByFilter()")
    class FindByFilterTests {

        @Test
        @DisplayName("null filter returns empty list")
        void nullFilterReturnsEmptyList() {
            assertThat(billService.findBillsByFilter(null)).isEmpty();
        }

        @Test
        @DisplayName("valid filter returns bills")
        void validFilterReturnsBills() {
            Bill unique = MockDataProvider.createMockBill(mockUserEntity, mockCategory);
            unique.setId(4);
            unique.setDescription("UniqueDesc");

            when(userService.findUsersByNameIn(List.of(mockUserEntity.getName())))
                .thenReturn(List.of(mockUserEntity));
            when(billRepository.findByFilter(
                "UniqueDesc",
                List.of(mockCategory.getId()),
                List.of(mockUserEntity),
                Sort.by(Sort.Direction.DESC, "date")
            )).thenReturn(List.of(unique));

            BillDTO dto = new BillDTO(
                unique.getId(), unique.getAmount(), unique.getOwnAmount(),
                unique.getDescription(), unique.getDate(), unique.isPaid(),
                unique.getCategory().getId(), unique.getOwner().getId(),
                unique.getOwner().getName()
            );
            when(billConverter.billToDTO(unique)).thenReturn(dto);

            SearchFilter filter = new SearchFilter(
                "UniqueDesc",
                List.of(mockCategory.getId()),
                null,
                List.of(mockUserEntity.getName())
            );
            List<BillDTO> result = billService.findBillsByFilter(filter);

            assertThat(result).hasSize(1)
                .first()
                .extracting(BillDTO::id)
                .isEqualTo(4);
        }

    }

    @Nested
    @DisplayName("payDebt()")
    class PayDebtTests {

        @Test
        @DisplayName("pays debt, evicts cache, and returns recent bills")
        void payDebtReturnsRecent() {
            BillDTO dto = MockDataProvider.createMockBillDTO(mockUserEntity, mockCategory);
            Bill toSave = MockDataProvider.createMockBill(mockUserEntity, mockCategory);
            when(billConverter.dtoToBill(dto)).thenReturn(toSave);

            Bill saved = MockDataProvider.createMockBill(mockUserEntity, mockCategory);
            saved.setId(5);
            when(billRepository.save(toSave)).thenReturn(saved);

            BillDTO savedDto = new BillDTO(
                saved.getId(), saved.getAmount(), saved.getOwnAmount(),
                saved.getDescription(), saved.getDate(), saved.isPaid(),
                saved.getCategory().getId(), saved.getOwner().getId(),
                saved.getOwner().getName()
            );
            when(billConverter.billToDTO(saved)).thenReturn(savedDto);

            Bill recent = MockDataProvider.createMockBill(mockUserEntity, mockCategory);
            recent.setId(6);

            List<BillDTO> result = billService.payDebt(dto);

            verify(billRepository).payDebt();
            assertThat(result).extracting(BillDTO::id)
                .containsExactly(5);
        }

    }

    @Nested
    @DisplayName("findAllByYear()")
    class FindAllByYearTests {

        @Test
        @DisplayName("returns bills for given year")
        void returnsBillsForYear() {
            Bill b2023 = MockDataProvider.createMockBill(mockUserEntity, mockCategory);
            b2023.setId(7);
            b2023.setDate(LocalDate.of(2023, 3, 1));
            when(billRepository.findAllByYear(2023)).thenReturn(List.of(b2023));

            BillDTO dto = new BillDTO(
                b2023.getId(), b2023.getAmount(), b2023.getOwnAmount(),
                b2023.getDescription(), b2023.getDate(), b2023.isPaid(),
                b2023.getCategory().getId(), b2023.getOwner().getId(),
                b2023.getOwner().getName()
            );
            when(billConverter.billToDTO(b2023)).thenReturn(dto);

            List<BillDTO> result = billService.findAllByYear(2023);

            assertThat(result).hasSize(1)
                .first()
                .extracting(BillDTO::id)
                .isEqualTo(7);
        }

        @Nested
        @DisplayName("getStats()")
        class GetStatsTests {

            @Test
            @DisplayName("null filter returns empty list")
            void nullFilterReturnsEmptyList() {
                assertThat(billService.getStats(null)).isEmpty();
            }

            @Test
            @DisplayName("empty range returns empty list")
            void emptyRangeReturnsEmptyList() {
                StatsFilter filter = new StatsFilter(List.of(), mockUserEntity.getName());
                assertThat(billService.getStats(filter)).isEmpty();
            }

            @Test
            @DisplayName("blank username returns empty list")
            void blankUsernameReturnsEmptyList() {
                StatsFilter filter = new StatsFilter(List.of("abcde"), "");
                assertThat(billService.getStats(filter)).isEmpty();
            }

        }

    }

    @Nested
    @DisplayName("findByUserId()")
    class FindByUserIdTests {

        @Test
        @DisplayName("existing user returns bills")
        void existingUserReturnsBills() {
            User another = MockDataProvider.createMockUser(mockGroup);
            another.setId(2);
            when(userService.findUserById(2)).thenReturn(Optional.of(another));
            Bill u1 = MockDataProvider.createMockBill(another, mockCategory);
            u1.setId(11);
            when(billRepository.findBillsByOwner(another)).thenReturn(List.of(u1));

            BillDTO dto = new BillDTO(
                u1.getId(), u1.getAmount(), u1.getOwnAmount(),
                u1.getDescription(), u1.getDate(), u1.isPaid(),
                u1.getCategory().getId(), u1.getOwner().getId(),
                u1.getOwner().getName()
            );
            when(billConverter.billToDTO(u1)).thenReturn(dto);

            List<BillDTO> result = billService.findBillsByUserId(2);

            assertThat(result).hasSize(1)
                .first()
                .extracting(BillDTO::id)
                .isEqualTo(11);
        }

        @Test
        @DisplayName("missing user throws exception")
        void missingUserThrowsException() {
            when(userService.findUserById(3)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> billService.findBillsByUserId(3))
                .isInstanceOf(RuntimeException.class);
        }

    }

    @Nested
    @DisplayName("findUserDebtByUserId()")
    class FindUserDebtByIdTests {

        @Test
        @DisplayName("returns user debt")
        void returnsUserDebt() {
            when(billRepository.findUserDebtByUserId(anyInt())).thenReturn(123.45);
            double debt = billService.findUserDebtByUserId(4);
            assertThat(debt).isEqualTo(123.45);
        }

    }

    @Test
    @DisplayName("deleteBillById returns true on successful delete")
    void deleteBillByIdReturnsTrue() {
        Integer billId = 42;
        doNothing().when(billRepository).deleteById(billId);
        boolean result = billService.deleteBillById(billId);
        assertThat(result).isTrue();
        verify(billRepository).deleteById(billId);
    }

    @Test
    @DisplayName("deleteBillById returns false when delete throws exception")
    void deleteBillByIdReturnsFalseOnException() {
        Integer billId = 99;
        doThrow(new RuntimeException("Exception")).when(billRepository).deleteById(billId);
        boolean result = billService.deleteBillById(billId);
        assertThat(result).isFalse();
        verify(billRepository).deleteById(billId);
    }

}