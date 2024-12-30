package com.weshare.service;

import com.weshare.AbstractApplicationTest;
import com.weshare.dto.BillDTO;
import com.weshare.mocks.MockDataProvider;
import com.weshare.model.Bill;
import com.weshare.model.Category;
import com.weshare.model.SearchFilter;
import com.weshare.repository.BillRepository;
import com.weshare.repository.CategoryRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BillIntegrationTest extends AbstractApplicationTest {
    private static final String BASE_URL = "/api/bills";
    private static final String SEARCH_BILLS_URL = BASE_URL + "/search";
    private static final String DELETE_URL = BASE_URL + "/{id}";
    private List<Category> categories;

    @Autowired
    BillRepository billRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        this.categories = Stream.generate(() -> MockDataProvider.createMockCategory(group))
                .limit(6)
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
        Category category = categories.get(ThreadLocalRandom.current().nextInt(categories.size()));
        BillDTO bill = MockDataProvider.createMockBillDTO(user, category);

        Response response = RestAssured.given(requestSpecification)
                .body(bill)
                .post(BASE_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();

        BillDTO savedBill = response.as(BillDTO.class);

        assertThat(savedBill.id()).isNotNull();
        assertThat(savedBill.description()).isEqualTo(bill.description());
        assertThat(savedBill.amount()).isEqualTo(bill.amount());
        assertThat(savedBill.ownAmount()).isEqualTo(bill.ownAmount());
        assertThat(savedBill.paid()).isEqualTo(bill.paid());
        assertThat(savedBill.date()).isEqualTo(bill.date());
        assertThat(savedBill.categoryId()).isEqualTo(bill.categoryId());
        assertThat(savedBill.ownerId()).isEqualTo(bill.ownerId());
        assertThat(savedBill.ownerName()).isEqualTo(bill.ownerName());
    }

    @Test
    @DisplayName("Should return bills based on search params")
    void filterBillsByCriteria() {
        Category category = categories.get(ThreadLocalRandom.current().nextInt(categories.size()));
        Bill bill = MockDataProvider.createMockBill(user, category);
        bill.setDescription("SearchingBillByParams");
        billRepository.save(bill);

        generateBills(50);

        SearchFilter filter = new SearchFilter(bill.getDescription(), List.of(category.getId()), null, List.of(user.getName()));
        Response response = RestAssured.given(requestSpecification)
                .body(filter)
                .post(SEARCH_BILLS_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        List<BillDTO> returnedBills = response.jsonPath().getList(".", BillDTO.class);

        assertThat(returnedBills).hasSize(1);
        assertThat(returnedBills.getFirst().id()).isEqualTo(bill.getId());
    }

    @Test
    @DisplayName("Should delete bill by ID")
    void deleteBillById() {
        Category category = categories.get(ThreadLocalRandom.current().nextInt(categories.size()));
        Bill bill = MockDataProvider.createMockBill(user, category);
        Bill savedBill = billRepository.save(bill);

        generateBills(100);

        Response response = RestAssured.given(requestSpecification)
                .get(BASE_URL + "/{userId}", user.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        List<BillDTO> returnedBills = response.jsonPath().getList(".", BillDTO.class);

        assertThat(returnedBills).hasSize(101);
        assertThat(returnedBills)
                .extracting("id")
                .contains(savedBill.getId());

        response = RestAssured.given(requestSpecification)
                .delete(DELETE_URL, savedBill.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract()
                .response();

        assertThat(response.asString()).isEmpty();
        assertThat(billRepository.findById(savedBill.getId())).isEmpty();
    }

    private void generateBills(int max) {
        Bill bill;
        Category category;
        for (int i = 0; i < max; i++) {
            category = categories.get(ThreadLocalRandom.current().nextInt(categories.size()));
            bill = MockDataProvider.createMockBill(user, category);
            billRepository.save(bill);
        }
    }
}