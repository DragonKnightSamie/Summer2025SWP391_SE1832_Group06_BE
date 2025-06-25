package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.todo.BlogDTO;
import com.gender_healthcare_system.services.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/blogs/public")
@AllArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // getAllBlogs
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllBlogsForAnyOne
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "blogId") String sort,
     @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(blogService.getAllBlogs(page, sort, order));
    }

    // rename API from getBlogsById to getBlogByIdForAnyOne
    // getBlogsById for any one
    @GetMapping("/{id}")
    public ResponseEntity<BlogDTO> getBlogByIdForAnyOne(@PathVariable int id) {
        BlogDTO blog = blogService.getBlogForCustomerById(id);
        if (blog != null) {
            return ResponseEntity.ok(blog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // searchBlogs
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBlogsForAnyOne
    (@RequestParam(defaultValue = "a") String keyword,
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "blogId") String sort,
     @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(blogService.searchBlogs(keyword, page, sort, order));
    }
}
