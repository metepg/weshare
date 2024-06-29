package com.weshare.repository;

import com.weshare.model.Category;
import com.weshare.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

   List<Category> findByGroup(Group group);
}
