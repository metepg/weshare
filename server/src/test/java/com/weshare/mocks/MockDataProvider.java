package com.weshare.mocks;

import com.weshare.dto.BillDTO;
import com.weshare.model.Bill;
import com.weshare.model.Category;
import com.weshare.model.Group;
import com.weshare.model.User;
import net.datafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

public class MockDataProvider {
    private static final Faker faker = new Faker();

    public static final String USER_NAME = "user";
    public static final String USER_PASSWORD = "password";
    public static final String ROLE = "Role1";

    public static Group createMockGroup() {
        Group group = new Group();
        group.setName(getRandomString() + " Group");
        return group;
    }

    public static User createMockUser(Group group) {
        User user = new User();
        user.setName(USER_NAME);
        user.setPassword(new BCryptPasswordEncoder().encode(USER_PASSWORD));
        user.setRole(ROLE);
        user.setGroup(group);
        group.setUsers(List.of(user));
        return user;
    }

    public static Bill createMockBill(User user, Category category) {
        double amount = getRandomAmount();
        return Bill.builder()
            .owner(user)
            .amount(amount)
            .ownAmount(getRandomOwnAmount(amount))
            .category(category)
            .paid(getRandomBoolean())
            .date(getRandomDate())
            .description(getRandomString())
            .build();
    }

    public static BillDTO createMockBillDTO(User user, Category category) {
        double amount = getRandomAmount();
        double ownAmount = getRandomOwnAmount(amount);

        return new BillDTO(
                null,
                amount,
                ownAmount,
                getRandomString(),
                LocalDate.now(),
                getRandomBoolean(),
                category.getId(),
                user.getId(),
                user.getName()
        );
    }

    public static Category createMockCategory(Group group) {
        Category category = new Category();
        category.setDescription("Category: " + getRandomString());
        category.setGroup(group);
        return category;
    }

    private static double getRandomAmount() {
        return faker.number().randomDouble(2, 100, 500);
    }

    private static double getRandomOwnAmount(double amount) {
        return faker.number().randomDouble(2, 100, (int) Math.floor(amount));
    }

    private static LocalDate getRandomDate() {
        return faker.timeAndDate().birthday(30, 31);
    }

    private static String getRandomString() {
        return faker.commerce().productName();
    }

    private static boolean getRandomBoolean() {
        return faker.bool().bool();
    }
}