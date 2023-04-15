/**
 * Copy contents of this class to WeShareApplication.java and start application
 * normally
 * It generates bills and users for for development purpose.
 * Running this is also the only way to add users to database.
 */

//package com.weshare;
//
//import com.weshare.model.Bill;
//import com.weshare.model.ERole;
//import com.weshare.model.User;
//import com.weshare.repository.IUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//
//@SpringBootApplication
//@EnableMongoRepositories
//public class WeshareApplication implements CommandLineRunner {
//
//    private static final String user1 = "User1";
//    private static final String user2 = "User2";
//    private static final String[] categories = {"Ruoka", "Muut", "Bensa", "Laskut"};
//    private static final int fromYear = 2021;
//    private static final int toYear = 2023;
//    private static final int daysBetweenBills = 4;
//    private final List<Bill> bills = new ArrayList<>();
//
//    @Autowired
//    IUserRepository userRepository;
//    @Autowired
//    MongoTemplate db;
//
//    public static void main(String[] args) {
//        SpringApplication.run(WeshareApplication.class, args);
//    }
//
//    public void run(String... args) {
////        deleteUsers();
////        createUsers();
//        deleteBills();
//        createBills();
//        System.out.println("-------------DONE-------------------------------\n");
//    }
//
//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    void deleteBills() {
//        System.out.println("-------------DELETING ALL BILLS-------------------------------\n");
//        db.remove(new Query(), "bills");
//    }
//
//    void deleteUsers() {
//        System.out.println("-------------DELETING ALL BILLS-------------------------------\n");
//        db.remove(new Query(), "users");
//    }
//
//    void createUsers() {
//        System.out.println("-------------CREATING USERS-------------------------------\n");
//        User testUser1 = new User(user1, encoder().encode("password1"));
//        Set<String> user1Roles = new HashSet<>();
//        user1Roles.add(ERole.ROLE1);
//        testUser1.setRoles(user1Roles);
//        userRepository.save(testUser1);
//
//        User testUser2 = new User(user2, encoder().encode("password2"));
//        Set<String> user2Roles = new HashSet<>();
//        user2Roles.add(ERole.ROLE2);
//        testUser2.setRoles(user2Roles);
//        userRepository.save(testUser2);
//    }
//
//    void createBills() {
//        System.out.println("-------------CREATING BILLS-------------------------------\n");
//        Collection<Bill> bills = generateBills();
//        db.insertAll(bills);
//    }
//
//    List<Bill> generateBills() {
//        int minAmount = 8;
//        int maxAmount = 60;
//        LocalDate startDate = LocalDate.of(fromYear, 1, 1);
//        LocalDate endDate = LocalDate.of(toYear, 1, 1);
//
//        while (startDate.isBefore(endDate)) {
//            Random random = new Random();
//            int randomNumber = random.nextInt(4);
//
//            String owner = randomNumber == 2 ? user1 : user2;
//            String description = "Test";
//            double amount = getAmount(minAmount, maxAmount);
//            String category = categories[randomNumber];
//            Date date = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//            bills.add(new Bill(owner, description, amount, category, date, amount / 2, false));
//            startDate = startDate.plusDays(daysBetweenBills);
//        }
//        return bills;
//    }
//
//    double getAmount(double minAmount, double maxAmount) {
//        Random r = new Random();
//        double amount = minAmount + (maxAmount - minAmount) * r.nextDouble();
//        amount = amount * 100;
//        amount = ((int) amount);
//        return amount / 100;
//    }
//
//}
