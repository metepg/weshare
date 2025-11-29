package com.weshare.service;

import com.weshare.TestcontainersConfig;
import com.weshare.dto.BillDTO;
import com.weshare.mocks.MockDataProvider;
import com.weshare.model.Bill;
import com.weshare.model.Category;
import com.weshare.model.SearchFilter;
import com.weshare.model.StatsFilter;
import com.weshare.repository.BillRepository;
import com.weshare.repository.CategoryRepository;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

//https://github.com/rest-assured/rest-assured/issues/1846
@Disabled("Until rest-assured is compatible with Groovy 5")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BillIntegrationTest extends TestcontainersConfig {
    private static final String BASE_URL = "/api/bills";
    private static final String SEARCH_BILLS_URL = BASE_URL + "/search";

    private static final int NUMBER_OF_BILLS = 100;
    private static final int NUMBER_OF_CATEGORIES = 6;

    private List<Category> categories;

    @Autowired
    BillRepository billRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        this.categories = Stream.generate(() -> MockDataProvider.createMockCategory(group))
                .limit(NUMBER_OF_CATEGORIES)
                .map(categoryRepository::save)
                .toList();

    }

    @AfterEach
    void tearDown() {
        billRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Save bill")
    void saveBill() {
        generateBillsForTests();
        Category category = categories.get(ThreadLocalRandom.current().nextInt(NUMBER_OF_CATEGORIES));
        BillDTO originalBill = MockDataProvider.createMockBillDTO(user, category);
        BillDTO savedBill = saveBill(originalBill);

        assertThat(savedBill.id()).isNotNull();
        assertThat(savedBill)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(originalBill);
    }

    @Test
    @DisplayName("Edit bill")
    void editBill() {
        BillDTO originalBill = createAndSaveBill();

        // Edit fields
        BillDTO modifiedBill = new BillDTO(
                originalBill.id(),
                10,
                5,
                "EDIT_BILL_TEST",
                LocalDate.now(),
                !originalBill.paid(),
                categories.get(ThreadLocalRandom.current().nextInt(categories.size())).getId(),
                originalBill.ownerId(),
                originalBill.ownerName()
        );

        BillDTO returnedBill = given(requestSpecification)
                .body(modifiedBill)
                .put(BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .as(BillDTO.class);

        assertThat(returnedBill)
                .usingRecursiveComparison()
                .isEqualTo(modifiedBill);
    }

    @Test
    @DisplayName("Should return bills based on search params")
    void filterBillsByCriteria() {
        generateBillsForTests();
        Category category = categories.get(ThreadLocalRandom.current().nextInt(NUMBER_OF_CATEGORIES));
        Bill uniqueBill = MockDataProvider.createMockBill(user, category);
        uniqueBill.setDescription("SearchingBillByParams");
        billRepository.save(uniqueBill);

        SearchFilter filter = new SearchFilter(uniqueBill.getDescription(), List.of(category.getId()), null, List.of(user.getName()));
        List<BillDTO> returnedBills = given(requestSpecification)
                .body(filter)
                .post(SEARCH_BILLS_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .jsonPath()
                .getList(".", BillDTO.class);

        assertThat(returnedBills).hasSize(1);
        assertThat(returnedBills.getFirst().id()).isEqualTo(uniqueBill.getId());
    }

    @Test
    @DisplayName("Should delete bill by ID")
    void deleteBillById() {
        BillDTO savedBill = createAndSaveBill();

        Response response = given(requestSpecification)
                .delete(BASE_URL + "/{id}", savedBill.id())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract()
                .response();

        assertThat(response.asString()).isEmpty();
        assertThat(billRepository.findById(savedBill.id())).isEmpty();
    }

    @Test
    @DisplayName("Should return all recent bills for authorized user")
    void findRecentBills_shouldReturnAllBills() {
        generateBillsForTests();
        List<BillDTO> returnedBills = given(requestSpecification)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .jsonPath()
                .getList(".", BillDTO.class);

        assertThat(returnedBills).hasSize(NUMBER_OF_BILLS);
    }

    @Test
    @DisplayName("Should return FORBIDDEN when given invalid userId")
    void none() {
        given(requestSpecification)
            .when()
            .get(BASE_URL + "/user/{userId}", 0)
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Returns bills for given year")
    void returnsBillsForGivenYear() {
        generateBillsForTests();
        int year = LocalDate.now().getYear();

        List<BillDTO> result = given(requestSpecification)
            .when()
            .get(BASE_URL + "/statistics/{year}", year)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", BillDTO.class);

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Pays debt and returns updated bills")
    void paysDebtAndReturnsUpdatedBills() {
        BillDTO savedBill = createAndSaveBill();

        BillDTO modified = new BillDTO(
            savedBill.id(),
            savedBill.amount(),
            savedBill.ownAmount(),
            savedBill.description(),
            savedBill.date(),
            true,
            savedBill.categoryId(),
            savedBill.ownerId(),
            savedBill.ownerName()
        );

        List<BillDTO> result = given(requestSpecification)
            .body(modified)
            .post(BASE_URL + "/pay")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", BillDTO.class);

        assertThat(result).isNotEmpty();
        assertThat(result.getFirst().paid()).isTrue();
    }

    @Test
    @DisplayName("Returns stats for given user and range")
    void returnsStatsForUserAndRange() {
        generateBillsForTests();
        StatsFilter filter = new StatsFilter(
            List.of("2024", "2025"),
            user.getName()
        );

        List<BillDTO> result = given(requestSpecification)
            .body(filter)
            .get(BASE_URL + "/stats")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", BillDTO.class);

        assertThat(result).isNotEmpty();
    }

    private void generateBillsForTests() {
        List<Bill> bills = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_BILLS; i++) {
            Category category = categories.get(ThreadLocalRandom.current().nextInt(NUMBER_OF_CATEGORIES));
            Bill bill = MockDataProvider.createMockBill(user, category);
            bill.setDate(LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(30)));
            bills.add(bill);
        }
        billRepository.saveAll(bills);
    }

    private BillDTO saveBill(BillDTO bill) {
        return given(requestSpecification)
                .body(bill)
                .post(BASE_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response()
                .as(BillDTO.class);
    }

    private BillDTO createAndSaveBill() {
        Category category = categories.get(ThreadLocalRandom.current().nextInt(NUMBER_OF_CATEGORIES));
        BillDTO bill = MockDataProvider.createMockBillDTO(user, category);
        return given(requestSpecification)
                .body(bill)
                .post(BASE_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response()
                .as(BillDTO.class);
    }
}