package repository;

import com.weshare.Application;
import com.weshare.model.Group;
import com.weshare.model.User;
import com.weshare.repository.GroupRepository;
import com.weshare.repository.UserRepository;
import mocks.MockDataProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

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
    void tearDown() {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    void testFindUserByName() {
        Optional<User> foundUser = userRepository.findUserByName(testUser.getName());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(testUser.getName());
    }

    @Test
    void testFindUserById() {
        Optional<User> foundUser = userRepository.findUserById(testUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void testFindUsersByNameIn() {
        User anotherUser = MockDataProvider.createMockUser(testGroup);
        anotherUser.setName("Another User");
        userRepository.save(anotherUser);

        List<String> names = Arrays.asList(testUser.getName(), "Another User");
        Optional<List<User>> foundUsers = userRepository.findUsersByNameIn(names);

        assertThat(foundUsers).isPresent();
        assertThat(foundUsers.get()).hasSize(2);
        assertThat(foundUsers.get().get(0).getName()).isIn(names);
        assertThat(foundUsers.get().get(1).getName()).isIn(names);
    }

    @Test
    void testSaveUser() {
        User newUser = MockDataProvider.createMockUser(testGroup);
        newUser.setName("New User");
        userRepository.save(newUser);

        Optional<User> foundUser = userRepository.findUserByName("New User");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("New User");
    }

    @Test
    void testDeleteUser() {
        userRepository.delete(testUser);
        Optional<User> foundUser = userRepository.findUserById(testUser.getId());
        assertThat(foundUser).isNotPresent();
    }

    @Test
    @Disabled("Fixing this later")
    void testUniqueUserNameAcrossDifferentGroups() {
        Group group = groupRepository.save(MockDataProvider.createMockGroup());
        User userInAnotherGroup = MockDataProvider.createMockUser(group);

        userRepository.save(userInAnotherGroup);

        Optional<User> foundUser = userRepository.findUserById(userInAnotherGroup.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(testUser.getName());
        assertThat(foundUser.get().getGroup().getId()).isEqualTo(group.getId());
    }
}