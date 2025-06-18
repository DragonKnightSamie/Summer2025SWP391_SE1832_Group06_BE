package com.gender_healthcare_system.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.todo.TestingServiceBookingDTO;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import com.gender_healthcare_system.entities.todo.TestingServicePayment;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.login.LoginRequest;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingCompletePayload;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingConfirmPayload;
import com.gender_healthcare_system.services.JwtService;
import com.gender_healthcare_system.services.TestingServicePaymentService;
import com.gender_healthcare_system.services.StaffService;
import com.gender_healthcare_system.services.TestingServiceBookingService;
import lombok.AllArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff")
@AllArgsConstructor

public class StaffController {

    private final TestingServicePaymentService testingServicePaymentService;
    
    private final StaffService staffService;
    
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TestingServiceBookingService testingServiceBookingService;

    //Staff login
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


    ///////////////////////////// Manage Payments ///////////////////////////////


    // 1. Get all payments
    @GetMapping("/payments/")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public List<TestingServicePayment> getAllPayments() {
        return testingServicePaymentService.getAllPayments();
    }

    // 2. Get payment by ID
    @GetMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public TestingServicePayment getPaymentById(@PathVariable int id) {
        return testingServicePaymentService.getPaymentById(id);
    }

    // 3. Create new testingServicePayment
    @PostMapping("/payments/")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public TestingServicePayment createPayment(@RequestBody TestingServicePayment testingServicePayment) {
        return testingServicePaymentService.createPayment(testingServicePayment);
    }

    // 4. Update testingServicePayment
    @PutMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public TestingServicePayment updatePayment(@PathVariable int id, @RequestBody TestingServicePayment testingServicePayment) {
        return testingServicePaymentService.updatePayment(id, testingServicePayment);
    }

    // 5. Delete payment
    @DeleteMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public void deletePayment(@PathVariable int id) {
        testingServicePaymentService.deletePayment(id);
    }

    // 6. Get payments by staff ID
    /*@GetMapping("/payments/by-staff/{staffId}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public List<TestingServicePayment> getPaymentsByStaffId(@PathVariable int staffId) {
        return paymentService.getPaymentsByStaffId(staffId);
    }*/

    // 7. Get payments by status
    @GetMapping("/payments/status/{status}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public List<TestingServicePayment> getPaymentsByStatus(@PathVariable String status) {
        try {
            PaymentStatus parsedStatus = PaymentStatus.valueOf(status.toUpperCase());
            return testingServicePaymentService.getPaymentsByStatus(parsedStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + status);
        }
    }

    // 8. Update payment status
    @PutMapping("/payments/{id}/update-status")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public TestingServicePayment updatePaymentStatus(@PathVariable int id, @RequestParam String newStatus) {
        try {
            PaymentStatus status = PaymentStatus.valueOf(newStatus.toUpperCase());
            return testingServicePaymentService.updatePaymentStatus(id, status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + newStatus);
        }
    }

    /// ////////////////////////////////////// Manage Testing Service Bookings //////////////////////////////////////

    //get by id
    @GetMapping("/testing-service-bookings/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public TestingServiceBookingDTO getTestingServiceBookingById
    (@PathVariable int id) throws JsonProcessingException {
        return testingServiceBookingService.getTestingServiceBookingDetailsById(id);
    }

    //Lấy danh sách (có phân trang + sắp xếp)
    // vd:/staff/testing-service-history?page=0&sortField=createdAt&sortOrder=desc
    @GetMapping("/testing-service-bookings/staff/{staffId}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public Map<String, Object> getAllTestingServiceBookings
    (@PathVariable int staffId,
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "serviceBookingId") String sortField,
     @RequestParam(defaultValue = "asc") String sortOrder) {

        return testingServiceBookingService
                .getAllTestingServiceBookingsByStaffId(staffId, page, sortField, sortOrder);
    }

    //Lấy danh sách booking có status pending (có phân trang + sắp xếp)
    @GetMapping("/testing-service-bookings/pending-list")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public Map<String, Object> getAllPendingTestingServiceBookings
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "serviceBookingId") String sortField,
     @RequestParam(defaultValue = "asc") String sortOrder) {

        return testingServiceBookingService
                .getAllPendingTestingServiceBookings(page, sortField, sortOrder);
    }

    // Update
    @PutMapping("/testing-service-bookings/{id}/confirm")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public void confirmTestingServiceBooking
    (@PathVariable int id,
     @RequestBody TestingServiceBookingConfirmPayload payload) {

        testingServiceBookingService.confirmTestingServiceBooking(id, payload);
    }

    // Update
    @PutMapping("/testing-service-bookings/{id}/complete")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public void completeTestingServiceBooking
    (@PathVariable int id,
     @RequestBody TestingServiceBookingCompletePayload payload) throws JsonProcessingException {

        testingServiceBookingService.completeTestingServiceBooking(id, payload);
    }

    @DeleteMapping("/testing-service-bookings/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public void deleteTestingServiceBooking(@PathVariable int id) {
        testingServiceBookingService.deleteTestingServiceBooking(id);
    }
}
