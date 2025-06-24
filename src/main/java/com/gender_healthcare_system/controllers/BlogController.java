package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.todo.BlogDTO;
import com.gender_healthcare_system.services.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/blogs")
@AllArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // getAllBlogs
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllBlogs
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "blogId") String sort,
     @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(blogService.getAllBlogs(page, sort, order));
    }

    // rename API from getBlogsById to getBlogByIdForAnyOne
    // getBlogsById for any one
    @GetMapping("/public/{id}")
    public ResponseEntity<BlogDTO> getBlogByIdForAnyOne(@PathVariable int id) {
        BlogDTO blog = blogService.getBlogForCustomerById(id);
        if (blog != null) {
            return ResponseEntity.ok(blog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // move API from ManagerController and rename it to getBlogByIDForManager
    // getBlogsById
    // Manager only
    @GetMapping("/manager/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<BlogDTO> getBlogByIdForManager(@PathVariable int id) {
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    // searchBlogs
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBlogs
    (@RequestParam String keyword,
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "blogId") String sort,
     @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(blogService.searchBlogs(keyword, page, sort, order));
    }
}
