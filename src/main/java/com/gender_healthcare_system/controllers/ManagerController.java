package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.*;
import com.gender_healthcare_system.entities.enu.AccountStatus;
import com.gender_healthcare_system.entities.todo.Blog;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.*;
import com.gender_healthcare_system.services.*;
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

    private final StaffService staffService;

    private final CustomerService customerService;

    private final ConsultantService consultantService;

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


    /////////////////////////// Manage Blogs /////////////////////////////////////


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
    @PostMapping("/blogs/create")
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
    @PutMapping("/blogs/update/{id}")
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
    @DeleteMapping("/blogs/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> deleteBlog(@PathVariable int id) {
        try {
            blogService.deleteBlog(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /////////////////////////// Manage Consultants /////////////////////////////////////


    //Manager get all Consultants
    @GetMapping("/consultants/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<ConsultantsDTO>> getAllConsultants() {

        return ResponseEntity.ok(consultantService.getAllConsultants());
    }

    //Manager get Consultant details by id
    @GetMapping("/consultants/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ConsultantDetailsDTO>
    getConsultantDetailsById(@PathVariable int id) {

        ConsultantDetailsDTO consultantDetails = consultantService.getConsultantDetails(id);
        if (consultantDetails != null) {
            return ResponseEntity.ok(consultantDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Manager create a new Consultant
    @PostMapping("/consultants/register")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> createConsultantAccount
            (@RequestBody ConsultantRegisterPayload payload) {

        accountService.createConsultantAccount(payload);
        return ResponseEntity.ok("Staff account created successfully");
    }

    //Manager update Consultant status
    @PostMapping("/consultants/update_status/{consultantId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateConsultantStatus
    (@PathVariable int consultantId, @RequestParam AccountStatus status) {

        accountService.updateCustomerStatus(consultantId, status);
        return ResponseEntity.ok("Consultant account status updated successfully");
    }

    //Manager delete Consultant from system
    @DeleteMapping("/consultants/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteConsultantAccount(@PathVariable int id) {

        accountService.deleteConsultantById(id);
        return ResponseEntity.ok("Consultant account deleted successfully");
    }


    /////////////////////////// Manage Staffs /////////////////////////////////////


    //Manager get all Staffs
    @GetMapping("/staffs/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<StaffDTO>> getAllStaffs() {
        return ResponseEntity.ok(staffService.getAllStaffs());
    }

    //Manager get staff details by id
    @GetMapping("/staffs/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable int id) {
        StaffDTO staffDetails = staffService.getStaffById(id);
        if (staffDetails != null) {
            return ResponseEntity.ok(staffDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //MANAGER CREATE STAFF ACCOUNT
    @PostMapping("/staffs/register")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> createStaffAccount(@RequestBody StaffRegisterPayload payload) {
        accountService.createStaffAccount(payload);
        return ResponseEntity.ok("Staff account created successfully");
    }

    //Manager update Staff infos
    @PutMapping("/staffs/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateStaffAccount
            (@PathVariable int id, @RequestBody StaffUpdatePayload payload) {

        staffService.updateStaffAccount(id, payload);
        return ResponseEntity.ok("Staff details updated successfully");
    }

    //Manager delete staff from system
    @DeleteMapping("/staffs/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteStaffAccount(@PathVariable int id) {

        accountService.deleteStaffById(id);
        return ResponseEntity.ok("Staff account deleted successfully");
    }


    /////////////////////////// Manage Customer /////////////////////////////////////


    //Get all Customers
    @GetMapping("/customers/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<ManagerCustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    //Get customer details by id
    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ManagerCustomerDTO> getCustomerById(@PathVariable int id) {
        ManagerCustomerDTO customerDetails = customerService.getCustomerById(id);
        if (customerDetails != null) {
            return ResponseEntity.ok(customerDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Manager change customer status
    @PostMapping("/customers/update_status/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateCustomerStatus
    (@PathVariable int customerId, @RequestParam AccountStatus status) {

        accountService.updateCustomerStatus(customerId, status);
        return ResponseEntity.ok("Customer account status updated successfully");
    }

    //Manager delete customer from system
    @DeleteMapping("/customers/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteCustomerAccount(@PathVariable int id) {

        accountService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer account deleted successfully");
    }
}
