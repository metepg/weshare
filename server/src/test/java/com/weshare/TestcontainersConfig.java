package com.weshare;

import com.weshare.mocks.MockDataProvider;
import com.weshare.model.Group;
import com.weshare.model.User;
import com.weshare.repository.GroupRepository;
import com.weshare.repository.UserRepository;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.test.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import static io.restassured.RestAssured.given;

/**
 * Abstract base class for integration tests using Testcontainers and Spring Boot.
 * <p>
 * Provides:
 * <ul>
 *   <li>Test user and group setup</li>
 *   <li>Pre-configured RestAssured {@link RequestSpecification}</li>
 * </ul>
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    useMainMethod = SpringBootTest.UseMainMethod.ALWAYS
)
@ActiveProfiles("test")
public abstract class TestcontainersConfig {

    protected RequestSpecification requestSpecification;
    protected User user;
    protected Group group;

    @LocalServerPort
    int port;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    protected void setupEnvironment() {
        // Set required data for login
        this.group = groupRepository.save(MockDataProvider.createMockGroup());
        this.user = userRepository.save(MockDataProvider.createMockUser(group));

        Response initialResponse = given()
            .port(port)
            .get("/");

        String sessionCookie = initialResponse.getCookie("JSESSIONID");
        String xsrfToken = initialResponse.getCookie("XSRF-TOKEN");

        // Login with credentials and XSRF token
        Response loginResponse = given()
            .port(port)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .cookie("JSESSIONID", sessionCookie)
            .cookie("XSRF-TOKEN", xsrfToken)
            .header("X-XSRF-TOKEN", xsrfToken)
            .formParam("username", MockDataProvider.USER_NAME)
            .formParam("password", MockDataProvider.USER_PASSWORD)
            .post("/login");

        String jsessionid = loginResponse.getCookie("JSESSIONID");
        String loggedInXsrfToken = loginResponse.getCookie("XSRF-TOKEN");

        requestSpecification = new RequestSpecBuilder()
            .setPort(port)
            .addCookie("JSESSIONID", jsessionid)
            .addCookie("XSRF-TOKEN", loggedInXsrfToken)
            .addHeader("X-XSRF-TOKEN", loggedInXsrfToken)
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @AfterEach
    protected void resetEnvironment() {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }
}


