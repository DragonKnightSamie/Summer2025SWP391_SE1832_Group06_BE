package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.AdminLoginResponse;

import com.gender_healthcare_system.dtos.ManagerDTO;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.LoginRequest;
import com.gender_healthcare_system.payloads.ManagerRegisterPayload;
import com.gender_healthcare_system.payloads.ManagerUpdatePayload;
import com.gender_healthcare_system.services.AccountService;
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

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final ManagerService managerService;

    private final AccountService accountService;

    //Admin login
    @PostMapping("/login")
    public AdminLoginResponse login(@RequestBody LoginRequest loginRequest) {
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
                        .getAuthority().equals("ROLE_ADMIN"));

        if(!hasRole) {
            throw new UsernameNotFoundException
                    ("Access denied for non-admin user");
        }

        AdminLoginResponse adminLoginDetails = new AdminLoginResponse();

        AccountInfoDetails account =
                (AccountInfoDetails) authentication.getPrincipal();
        int id = account.getId();

        adminLoginDetails.setId(id);
        adminLoginDetails.setUsername(loginRequest.getUsername());

        String jwtToken = jwtService.generateToken(loginRequest.getUsername());
        adminLoginDetails.setToken(jwtToken);

        return adminLoginDetails;
    }



    /////////////////////////// Manage Managers /////////////////////////////////////


    //Admin get all Managers
    @GetMapping("/managers/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllManagers
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "managerId") String sort,
     @RequestParam(defaultValue = "asc") String order ) {

        return ResponseEntity.ok(managerService.getAllManagers(page, sort, order));
    }

    //Admin get Manager details by id
    @GetMapping("/managers/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ManagerDTO>
    getManagerDetailsById(@PathVariable int id) {

        return ResponseEntity.ok(managerService.getManagerDetails(id));
    }

    //Admin create a new Manager
    @PostMapping("/managers/register")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createManagerAccount
    (@RequestBody ManagerRegisterPayload payload) {

        accountService.createManagerAccount(payload);
        return ResponseEntity.ok("Manager account created successfully");
    }

    //Admin update Manager details
    @PostMapping("/managers/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateManagerDetails
    (@PathVariable int id, @RequestBody ManagerUpdatePayload payload) {

        managerService.updateManagerDetails(id, payload);
        return ResponseEntity.ok("Manager profile updated successfully");
    }

    //Admin delete Manager from system
    @DeleteMapping("/managers/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteManagerAccount(@PathVariable int id) {

        accountService.deleteConsultantById(id);
        return ResponseEntity.ok("Manager account deleted successfully");
    }
}
