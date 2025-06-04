package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.entities.todo.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepo extends JpaRepository<Blog, Integer> {

    List<Blog> findByTitleContainingIgnoreCase(String keyword);
}

