package com.weshare.controller;

import com.weshare.model.Category;
import com.weshare.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CategoryService categoryService;

    private static final UUID GROUP_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final String BASE_URL = "/api/categories";

    @Test
    @DisplayName("Returns list of categories")
    @WithMockUser(roles = {"role1"})
    void returnsCategoriesForAuthorizedUser() throws Exception {
        String description = "Test";
        int id = 11;
        Category cat = new Category();
        cat.setId(id);
        cat.setDescription(description);
        List<Category> cats = List.of(cat);

        when(categoryService.findByGroupId(GROUP_ID)).thenReturn(cats);

        mockMvc.perform(get(BASE_URL + "/" + GROUP_ID)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(id))
            .andExpect(jsonPath("$[0].description").value(description));
    }

}
