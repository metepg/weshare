package com.weshare.repository;

import com.weshare.Application;
import com.weshare.model.Bill;
import com.weshare.model.Category;
import com.weshare.model.Group;
import com.weshare.model.User;
import com.weshare.mocks.MockDataProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class BillRepositoryTest {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        Group testGroup = groupRepository.save(MockDataProvider.createMockGroup());
        testUser = userRepository.save(MockDataProvider.createMockUser(testGroup));
        testCategory = categoryRepository.save(MockDataProvider.createMockCategory(testGroup));
    }

    @AfterEach
    void clear() {
        billRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveBill() {
        Bill bill = MockDataProvider.createMockBill(testUser, testCategory);
        billRepository.save(bill);

        Bill savedBill = billRepository.findAll().getFirst();
        assertThat(savedBill.getDescription()).isEqualTo(bill.getDescription());
        assertThat(savedBill.getAmount()).isEqualTo(bill.getAmount());
        assertThat(savedBill.getOwnAmount()).isEqualTo(bill.getOwnAmount());
        assertThat(savedBill.getOwner().getName()).isEqualTo(bill.getOwner().getName());
        assertThat(billRepository.count()).isEqualTo(1);
    }

    @Test
    void testEditBill() {
        Bill bill = MockDataProvider.createMockBill(testUser, testCategory);
        billRepository.save(bill);

        Bill savedBill = billRepository.findAll().getFirst();
        savedBill.setDescription("Updated Description");
        savedBill.setAmount(200.0);
        billRepository.save(savedBill);

        Bill updatedBill = billRepository.findById(savedBill.getId()).orElseThrow();
        assertThat(updatedBill.getDescription()).isEqualTo(savedBill.getDescription());
        assertThat(updatedBill.getAmount()).isEqualTo(savedBill.getAmount());
    }

    @Test
    void testDeleteBill() {
        Bill bill = MockDataProvider.createMockBill(testUser, testCategory);
        billRepository.save(bill);

        Bill savedBill = billRepository.findAll().getFirst();
        billRepository.delete(savedBill);

        assertThat(billRepository.findById(savedBill.getId())).isEmpty();
        assertThat(billRepository.count()).isEqualTo(0);
    }

}
