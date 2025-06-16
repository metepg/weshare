package com.weshare.service;

import com.weshare.model.Category;
import com.weshare.model.Group;
import com.weshare.repository.CategoryRepository;
import com.weshare.repository.GroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    GroupRepository groupRepository;

    @InjectMocks
    CategoryService categoryService;

    @Test
    @DisplayName("Returns categories when group exists")
    void returnsCategoriesWhenGroupExists() {
        UUID groupId = UUID.randomUUID();
        Group group = new Group();
        Category cat = new Category();
        List<Category> expected = List.of(cat);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(categoryRepository.findByGroup(group)).thenReturn(expected);

        List<Category> result = categoryService.findByGroupId(groupId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Returns empty list when group not found")
    void returnsEmptyListWhenGroupNotFound() {
        UUID groupId = UUID.randomUUID();

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        List<Category> result = categoryService.findByGroupId(groupId);

        assertThat(result).isEmpty();
    }
}