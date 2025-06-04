package com.weshare.controller;

import com.weshare.model.Category;
import com.weshare.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{groupId}")
    @PreAuthorize("hasAnyRole(@ERole.role1, @ERole.role2)")
    public List<Category> findByGroupId(@PathVariable UUID groupId) {
        return categoryService.findByGroupId(groupId);
    }

}
