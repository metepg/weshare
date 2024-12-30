package com.weshare;

import com.weshare.mocks.MockDataProvider;
import com.weshare.model.Group;
import com.weshare.model.User;
import com.weshare.repository.GroupRepository;
import com.weshare.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
            "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///",
            "Role1=Role1",
            "Role2=Role2"
        })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractApplicationTest {

    protected RequestSpecification requestSpecification;
    protected User user;
    protected Group group;

    @LocalServerPort
    int port;

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeAll
    void setupEnvironment() {
        // Set required data for login
        this.group = groupRepository.save(MockDataProvider.createMockGroup());
        this.user = userRepository.save(MockDataProvider.createMockUser(group));

        // Login and get session cookie
        String sessionCookie = RestAssured.given()
                .port(port)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .formParam("username", MockDataProvider.USER_NAME)
                .formParam("password", MockDataProvider.USER_PASSWORD)
                .post("/login")
                .getCookie("JSESSIONID");

        requestSpecification = new RequestSpecBuilder()
                .setPort(port)
                .addCookie("JSESSIONID", sessionCookie) // Add session cookie for authenticated requests
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }
}

