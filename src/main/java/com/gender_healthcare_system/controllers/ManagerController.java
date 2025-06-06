package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.todo.Blog;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.LoginRequest;
import com.gender_healthcare_system.payloads.ManagerPayload;
import com.gender_healthcare_system.payloads.StaffPayload;
import com.gender_healthcare_system.services.AccountService;
import com.gender_healthcare_system.services.BlogService;
import com.gender_healthcare_system.services.JwtService;
import com.gender_healthcare_system.services.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
@AllArgsConstructor
public class ManagerController {

    private final BlogService blogService;

    private final ManagerService managerService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    //test
    //Manager registration
    @PostMapping("/register")
    public String register(@RequestBody ManagerPayload payload) {
        accountService.createManagerAccount(payload);
        return "Manager registered successfully";
    }

    //Manager login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Invalid Username or Password");
        }

        boolean hasRole = authentication
                .getAuthorities()
                .stream()
                .anyMatch(x -> x
                        .getAuthority().equals("ROLE_MANAGER"));

        if(!hasRole) {
            throw new UsernameNotFoundException
                    ("Access denied for non-manager user");
        }

        AccountInfoDetails account =
                (AccountInfoDetails) authentication.getPrincipal();
        int id = account.getId();

        LoginResponse loginDetails = managerService
                .getManagerLoginDetails(id);
        loginDetails.setUsername(loginRequest.getUsername());

        String jwtToken = jwtService.generateToken(loginRequest.getUsername());
        loginDetails.setToken(jwtToken);

        return loginDetails;
        //return jwtService.generateToken(loginRequest.getUsername());

    }

    // Manager logout
    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String logout(@RequestBody String token) {
        jwtService.isTokenBlacklisted(token);
        return "Logout successful";
    }

    //getAllBlogs
    @GetMapping("/blogs/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    //getBlogsById
    @GetMapping("/blogs/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Blog> getBlogById(@PathVariable int id) {
        Blog blog = blogService.getBlogById(id);
        if (blog != null) {
            return ResponseEntity.ok(blog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //searchBlogs
    @GetMapping("/blogs/search")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<Blog>> searchBlogs(@RequestParam String keyword) {
        return ResponseEntity.ok(blogService.searchBlogs(keyword));
    }

    //MANAGER CREATE BLOGS
    @GetMapping("/blogs/create")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        Blog newBlog = blogService.createBlog(blog);
        if (newBlog != null) {
            return ResponseEntity.ok(newBlog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //MANGER UPDATE BLOGS
    @GetMapping("/blogs/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Blog> updateBlog(@PathVariable int id, @RequestBody Blog blog) {
        Blog updatedBlog = blogService.updateBlog(id, blog);
        if (updatedBlog != null) {
            return ResponseEntity.ok(updatedBlog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //MANGER DELETE BLOGS
    @GetMapping("/blogs/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> deleteBlog(@PathVariable int id) {
        try {
            blogService.deleteBlog(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //MANAGER CREATE STAFF ACCOUNT
    @PostMapping("/registerStaff")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String createStaffAccount(@RequestBody StaffPayload payload) {
        accountService.createStaffAccount(payload);
        return "Staff account created successfully";
    }


}
