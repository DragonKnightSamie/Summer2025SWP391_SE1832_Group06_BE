package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import com.gender_healthcare_system.entities.todo.Payment;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.LoginRequest;
import com.gender_healthcare_system.services.JwtService;
import com.gender_healthcare_system.services.PaymentService;
import com.gender_healthcare_system.services.StaffService;
import lombok.AllArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@AllArgsConstructor
public class StaffController {

    private final PaymentService paymentService;
    
    private final StaffService staffService;
    
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    // 1. Get all payments
    @GetMapping("/payments/")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // 2. Get payment by ID
    @GetMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public Payment getPaymentById(@PathVariable int id) {
        return paymentService.getPaymentById(id);
    }

    // 3. Create new payment
    @PostMapping("/payments/")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    // 4. Update payment
    @PutMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public Payment updatePayment(@PathVariable int id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment);
    }

    // 5. Delete payment
    @DeleteMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public void deletePayment(@PathVariable int id) {
        paymentService.deletePayment(id);
    }

    // 6. Get payments by staff ID
    /*@GetMapping("/payments/by-staff/{staffId}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public List<Payment> getPaymentsByStaffId(@PathVariable int staffId) {
        return paymentService.getPaymentsByStaffId(staffId);
    }*/

    // 7. Get payments by status
    @GetMapping("/payments/status/{status}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public List<Payment> getPaymentsByStatus(@PathVariable String status) {
        try {
            PaymentStatus parsedStatus = PaymentStatus.valueOf(status.toUpperCase());
            return paymentService.getPaymentsByStatus(parsedStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + status);
        }
    }

    // 8. Update payment status
    @PutMapping("/payments/{id}/update-status")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public Payment updatePaymentStatus(@PathVariable int id, @RequestParam String newStatus) {
        try {
            PaymentStatus status = PaymentStatus.valueOf(newStatus.toUpperCase());
            return paymentService.updatePaymentStatus(id, status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + newStatus);
        }
    }

    // 9. Staff login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
        if (!authentication.isAuthenticated()) {

            throw new UsernameNotFoundException("Invalid username or password");
        }
            boolean hasRole = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(x -> x
                            .getAuthority().equals("ROLE_STAFF"));

            if(!hasRole){
                throw new UsernameNotFoundException
                        ("Access denied for non-staff user");
            }

            AccountInfoDetails account =
                    (AccountInfoDetails) authentication.getPrincipal();
            int id = account.getId();

            LoginResponse loginDetails = staffService.getStaffLoginDetails(id);
            loginDetails.setUsername(loginRequest.getUsername());

            String jwtToken = jwtService.generateToken(loginRequest.getUsername());
            loginDetails.setToken(jwtToken);

            return loginDetails;
           //return jwtService.generateToken(loginRequest.getUsername());

    }

    //10. Staff logout
    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public String logout(@RequestBody String token) {
        jwtService.isTokenBlacklisted(token);
        return "Logout successful";
    }
}
