package com.gender_healthcare_system.services;

import com.gender_healthcare_system.entities.todo.Blog;
import com.gender_healthcare_system.repositories.BlogRepo;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BlogService {

    private final BlogRepo blogRepo;

    //Get all
    public List<Blog> getAllBlogs() {
        return blogRepo.findAll();
    }

    //Get blog by id
    public Blog getBlogById(int id) {
        return blogRepo.findById(id).orElse(null);
    }

    //search blogs
    public List<Blog> searchBlogs(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return blogRepo.findAll();
        }
        return blogRepo.findByTitleContainingIgnoreCase(keyword);
    }

    //create blog
    public Blog createBlog(Blog blog) {
        return blogRepo.save(blog);
    }

    //update blog
    public Blog updateBlog(int id, Blog blog) {
        if (blogRepo.existsById(id)) {
            blog.setBlogId(id);
            return blogRepo.save(blog);
        }
        return null; // or throw an exception
    }

    //delete blog
    public void deleteBlog(int id) {
        if (blogRepo.existsById(id)) {
            blogRepo.deleteById(id);
        } else {
            throw new RuntimeException("Blog not found with id: " + id);
        }
    }

}
