package com.gender_healthcare_system.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.todo.TestingServiceBookingDTO;
import com.gender_healthcare_system.dtos.todo.TestingServiceResultDTO;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import com.gender_healthcare_system.entities.todo.TestingServicePayment;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.login.LoginRequest;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingCompletePayload;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingConfirmPayload;
import com.gender_healthcare_system.services.*;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/staff")
@AllArgsConstructor
public class StaffController {

    private final TestingServicePaymentService testingServicePaymentService;

    private final StaffService staffService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final MomoPaymentService momoPaymentService;

    private final TestingServiceResultService testingServiceResultService;

    private final TestingServiceBookingService testingServiceBookingService;

    //Staff login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login
    (@RequestBody @Valid LoginRequest loginRequest) {
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
                .anyMatch(x -> x.getAuthority().equals("ROLE_STAFF"));

        if (!hasRole) {
            throw new UsernameNotFoundException("Access denied for non-staff user");
        }

        AccountInfoDetails account = (AccountInfoDetails) authentication.getPrincipal();
        int id = account.getId();

        LoginResponse loginDetails = staffService.getStaffLoginDetails(id);
        loginDetails.setUsername(loginRequest.getUsername());

        String jwtToken = jwtService.generateToken(loginRequest.getUsername());
        loginDetails.setToken(jwtToken);

        return ResponseEntity.ok(loginDetails);
    }


    ///////////////////////////// Manage Payments ///////////////////////////////

    // 1. Get all payments
    @GetMapping("/payments/")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<List<TestingServicePayment>> getAllPayments() {
        return ResponseEntity.ok(testingServicePaymentService.getAllPayments());
    }

    // 2. Get payment by ID
    @GetMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<TestingServicePayment> getPaymentById(@PathVariable int id) {
        return ResponseEntity.ok(testingServicePaymentService.getPaymentById(id));
    }

    // 3. Create new testingServicePayment
    @PostMapping("/payments/")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<TestingServicePayment> createPayment(@RequestBody TestingServicePayment testingServicePayment) {
        return ResponseEntity.ok(testingServicePaymentService.createPayment(testingServicePayment));
    }

    // 4. Update testingServicePayment
    @PutMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<TestingServicePayment> updatePayment(@PathVariable int id, @RequestBody TestingServicePayment testingServicePayment) {
        return ResponseEntity.ok(testingServicePaymentService.updatePayment(id, testingServicePayment));
    }

    // 5. Delete payment
    @DeleteMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<String> deletePayment(@PathVariable int id) {
        testingServicePaymentService.deletePayment(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }

    // 7. Get payments by status
    @GetMapping("/payments/status/{status}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<List<TestingServicePayment>> getPaymentsByStatus(@PathVariable String status) {
        try {
            PaymentStatus parsedStatus = PaymentStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(testingServicePaymentService.getPaymentsByStatus(parsedStatus));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + status);
        }
    }

    // 8. Update payment status
    @PutMapping("/payments/{id}/update-status")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<TestingServicePayment> updatePaymentStatus(@PathVariable int id, @RequestParam String newStatus) {
        try {
            PaymentStatus status = PaymentStatus.valueOf(newStatus.toUpperCase());
            return ResponseEntity.ok(testingServicePaymentService.updatePaymentStatus(id, status));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + newStatus);
        }
    }

    /// /////////////////////////////// Manage Momo Payment Refund /////////////////////////////////

    /*
    //Get customer payment info
    @GetMapping("/payment-transaction/check-error")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<String> handleCallback(
            @RequestParam String orderId,
            @RequestParam String resultCode
    ) {
        if ("0".equals(resultCode)) {
            // payment success
            return ResponseEntity.ok("Payment successful for orderId " + orderId);
        } else {
            // payment failed
            return ResponseEntity.status(400).body("Payment failed with code: " + resultCode);
        }
    }*/

    /// ////////////////////////////////////// Manage Testing Service Bookings //////////////////////////////////////

    //get by id
    @GetMapping("/testing-service-bookings/{testingBookingid}/testing-templates")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<List<TestingServiceResultDTO>>
    getTestingBookingTestTemplatesById
    (@PathVariable int testingBookingid) {
        return ResponseEntity.ok(
                testingServiceResultService.getAllServiceResultsByBookingId(testingBookingid));
    }

    //get by id
    @GetMapping("/testing-service-bookings/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<TestingServiceBookingDTO>
    getTestingServiceBookingById(@PathVariable int id) throws JsonProcessingException {
        return ResponseEntity.ok(
                testingServiceBookingService.getTestingServiceBookingDetailsById(id));
    }

    //Lấy danh sách (có phân trang + sắp xếp)
    // vd:/staff/testing-service-history?page=0&sortField=createdAt&sortOrder=desc
    @GetMapping("/testing-service-bookings/staff/{staffId}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<Map<String, Object>> getAllTestingServiceBookings(
            @PathVariable int staffId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "serviceBookingId") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        return ResponseEntity.ok(testingServiceBookingService
                .getAllTestingServiceBookingsByStaffId(staffId, page, sortField, sortOrder));
    }

    //Lấy danh sách booking có status pending (có phân trang + sắp xếp)
    @GetMapping("/testing-service-bookings/pending-list")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<Map<String, Object>> getAllPendingTestingServiceBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "serviceBookingId") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        return ResponseEntity.ok(testingServiceBookingService
                .getAllPendingTestingServiceBookings(page, sortField, sortOrder));
    }

    //Lấy lịch xét nghiệm của staff với các expected start time đã full lịch đặt (5 người)
    @GetMapping("/testing-service-bookings/staff/{staffId}/check-schedule")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<List<LocalDateTime>> getStaffTestingScheduleForADay
    (@PathVariable int staffId,
     @Parameter(example = "05/06/2025")
     @RequestParam("date")
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
     LocalDate date) {

        return ResponseEntity.ok(testingServiceBookingService
                .getStaffScheduleForADay(staffId, date));
    }

    // Update
    @PutMapping("/testing-service-bookings/{id}/confirm")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<String> confirmTestingServiceBooking(
            @PathVariable int id,
            @RequestBody @Valid TestingServiceBookingConfirmPayload payload) {
        testingServiceBookingService.confirmTestingServiceBooking(id, payload);
        return ResponseEntity.ok("Testing Service Booking confirmed successfully");
    }

    // Update
    @PutMapping("/testing-service-bookings/{id}/complete")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<String> completeTestingServiceBooking(
            @PathVariable int id,
            @RequestBody @Valid TestingServiceBookingCompletePayload payload)
            throws JsonProcessingException {

        testingServiceBookingService.completeTestingServiceBooking(id, payload);
        return ResponseEntity.ok("Testing Service Booking marked as completed successfully");
    }

    @DeleteMapping("/testing-service-bookings/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<String> deleteTestingServiceBooking(@PathVariable int id) {
        testingServiceBookingService.deleteTestingServiceBooking(id);
        return ResponseEntity.ok("Testing Service Booking deleted successfully");
    }
}
