package mocks;

import com.weshare.model.Bill;
import com.weshare.model.Group;
import com.weshare.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MockDataProvider {

    public static Group createMockGroup() {
        Group group = new Group();
        group.setId(UUID.fromString("fb191d40-9db3-40aa-a061-f3dfac940867"));
        group.setName("Default Group");
        return group;
    }

    public static User createMockUser(Group group) {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setPassword("password");
        user.setRole("USER");
        user.setGroup(group);
        group.setUsers(List.of(user));
        return user;
    }

    public static Bill createMockBill(User user, Group group) {
        Bill bill = new Bill();
        bill.setId(1L);
        bill.setOwner(user);
        bill.setGroup(group);
        bill.setOwnAmount(50.0);
        bill.setAmount(100.0);
        bill.setCategory(1);
        bill.setDate(LocalDate.now());
        bill.setPaid(true);
        bill.setDescription("Test description");
        group.setBills(List.of(bill));
        return bill;
    }
}