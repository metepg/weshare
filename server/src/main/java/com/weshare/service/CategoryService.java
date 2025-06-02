package com.weshare.service;

import com.weshare.model.Category;
import com.weshare.model.Group;
import com.weshare.repository.CategoryRepository;
import com.weshare.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    @Cacheable(value = "categoriesByGroup", key = "#id")
    public List<Category> findByGroupId(UUID id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            return categoryRepository.findByGroup(group.get());
        } else {
            return List.of();
        }
    }
}
