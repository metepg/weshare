package com.weshare.controller;

import com.weshare.dto.UserDTO;
import com.weshare.service.BillService;
import com.weshare.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String BASE_URL = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private BillService billService;

    @Test
    void testFindCurrentUser() throws Exception {
        UserDTO userDTO = new UserDTO(1, "User", UUID.randomUUID(), "ROLE1");
        when(userService.findCurrentUser()).thenReturn(userDTO);

        mockMvc.perform(get(BASE_URL + "/current")
                .with(user("mock").roles("ROLE1")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("User"))
            .andExpect(jsonPath("$.groupId").exists())
            .andExpect(jsonPath("$.role").value("ROLE1"));
    }

    @Test
    void testFindUsers() throws Exception {
        UserDTO userDTO = new UserDTO(1, "User", UUID.randomUUID(), "ROLE1");
        when(userService.findUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get(BASE_URL)
                .with(user("mock").roles("ROLE1")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("User"))
            .andExpect(jsonPath("$[0].groupId").exists())
            .andExpect(jsonPath("$[0].role").value("ROLE1"));
    }

    @Test
    void testFindUserDebtByUserId() throws Exception {
        when(billService.findUserDebtByUserId(5)).thenReturn(123.45);

        mockMvc.perform(get(BASE_URL + "/5/debt")
                .with(user("mock").roles("ROLE1")))
            .andExpect(status().isOk())
            .andExpect(content().string("123.45"));
    }
}
