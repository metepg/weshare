package com.weshare.security;

import com.weshare.controller.BillController;
import com.weshare.model.ERole;
import com.weshare.service.BillEventService;
import com.weshare.service.BillService;
import com.weshare.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BillController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class, ERole.class})
@ActiveProfiles("test")
class CsrfTest {

    private static final String BASE_URL = "/api/bills";

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    BillService billService;
    @MockitoBean
    BillEventService billEventService;
    @MockitoBean
    SecurityService securityService;

    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithMockUser(roles = "Role1")
    void shouldRejectPostWithoutCsrf() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "Role1")
    void shouldAllowGetWithoutCsrf() throws Exception {
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk());
    }

    @Test
    void unauthenticatedShouldBeRejected() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

}
