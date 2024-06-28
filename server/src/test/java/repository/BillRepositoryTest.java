package repository;

import com.weshare.Application;
import com.weshare.model.Bill;
import com.weshare.model.Group;
import com.weshare.model.User;
import com.weshare.repository.BillRepository;
import com.weshare.repository.GroupRepository;
import com.weshare.repository.UserRepository;
import mocks.MockDataProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class BillRepositoryTest {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    private Group testGroup;
    private User testUser;

    @BeforeEach
    void setUp() {
        testGroup = groupRepository.save(MockDataProvider.createMockGroup());
        testUser = userRepository.save(MockDataProvider.createMockUser(testGroup));
    }

    @AfterEach
    void clear() {
        billRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveBill() {
        Bill bill = MockDataProvider.createMockBill(testUser, testGroup);
        billRepository.save(bill);

        Bill savedBill = billRepository.findAll().get(0);
        assertThat(savedBill.getDescription()).isEqualTo(bill.getDescription());
        assertThat(savedBill.getAmount()).isEqualTo(bill.getAmount());
        assertThat(savedBill.getOwnAmount()).isEqualTo(bill.getOwnAmount());
        assertThat(savedBill.getOwner().getName()).isEqualTo(bill.getOwner().getName());
        assertThat(savedBill.getGroup().getName()).isEqualTo(bill.getGroup().getName());
        assertThat(billRepository.count()).isEqualTo(1);
    }

    @Test
    void testEditBill() {
        Bill bill = MockDataProvider.createMockBill(testUser, testGroup);
        billRepository.save(bill);

        Bill savedBill = billRepository.findAll().get(0);
        savedBill.setDescription("Updated Description");
        savedBill.setAmount(200.0);
        billRepository.save(savedBill);

        Bill updatedBill = billRepository.findById(savedBill.getId()).orElseThrow();
        assertThat(updatedBill.getDescription()).isEqualTo(savedBill.getDescription());
        assertThat(updatedBill.getAmount()).isEqualTo(savedBill.getAmount());
    }

    @Test
    void testDeleteBill() {
        Bill bill = MockDataProvider.createMockBill(testUser, testGroup);
        billRepository.save(bill);

        Bill savedBill = billRepository.findAll().get(0);
        billRepository.delete(savedBill);

        assertThat(billRepository.findById(savedBill.getId())).isEmpty();
        assertThat(billRepository.count()).isEqualTo(0);
    }

    @Test
    void testSaveBillWithoutGroupShouldFail() {
        Bill bill = MockDataProvider.createMockBill(testUser, testGroup);
        bill.setGroup(null);

        assertThatThrownBy(() -> billRepository.save(bill))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
