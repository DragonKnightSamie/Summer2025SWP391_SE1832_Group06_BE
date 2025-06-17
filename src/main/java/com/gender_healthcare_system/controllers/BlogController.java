package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.todo.BlogDTO;
import com.gender_healthcare_system.services.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BlogController {

    private final BlogService blogService;

    //getAllBlogs
    @GetMapping("/blogs")
    public ResponseEntity<Map<String, Object>> getAllBlogs
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "blogId") String sort,
     @RequestParam(defaultValue = "asc") String order ) {
        return ResponseEntity.ok(blogService.getAllBlogs(page, sort, order));
    }

    //getBlogsById
    @GetMapping("/blogs/{id}")
    public ResponseEntity<BlogDTO> getBlogById(@PathVariable int id) {
        BlogDTO blog = blogService.getBlogForCustomerById(id);
        if (blog != null) {
            return ResponseEntity.ok(blog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //searchBlogs
    @GetMapping("/blogs/search")
    public ResponseEntity<Map<String, Object>> searchBlogs
    (@RequestParam String keyword,
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "blogId") String sort,
     @RequestParam(defaultValue = "asc") String order ) {
        return ResponseEntity.ok(blogService.searchBlogs(keyword, page, sort, order));
    }
}
