package com.gender_healthcare_system.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gender_healthcare_system.dtos.ConsultantConsultationDTO;
import com.gender_healthcare_system.dtos.ConsultantScheduleDTO;
import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.dtos.TestingServiceBookingDTO;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.*;
import com.gender_healthcare_system.services.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    
    private final AccountService accountService;

    private final CustomerService customerService;

    private final ConsultationService consultationService;
    
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TestingServiceBookingService testingServiceBookingService;

    @PostMapping("/register")
    public String register(@RequestBody CustomerPayload customerPayload) throws JsonProcessingException {
        accountService.createCustomerAccount(customerPayload);
        return "Customer registered successfully";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        if(!authentication.isAuthenticated()){
            throw new UsernameNotFoundException("Invalid Username or Password");
        }

        boolean hasRole = authentication
                .getAuthorities()
                .stream()
                .anyMatch(x -> x
                        .getAuthority().equals("ROLE_CUSTOMER"));

        if(!hasRole){
            throw new UsernameNotFoundException
                    ("Access denied for non-customer user");
        }

        AccountInfoDetails account = (AccountInfoDetails) authentication.getPrincipal();
            int id = account.getId();

            LoginResponse loginDetails = customerService.getCustomerLoginDetails(id);
            loginDetails.setUsername(loginRequest.getUsername());

            String jwtToken = jwtService.generateToken(loginRequest.getUsername());
            loginDetails.setToken(jwtToken);

            return loginDetails;
    }


    ////////////////////////////////// Manage Testing Service Bookings /////////////////////////

    //Get Service bookings by customer ID
    @GetMapping("/testing-service-bookings/customer/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public Map<String, Object> getTestingServiceBookingsByCustomerId
    (@PathVariable int customerId,
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "serviceBookingId") String sort,
     @RequestParam(defaultValue = "asc") String order ) {

        return testingServiceBookingService
                .getAllTestingServiceBookingsByCustomerId(customerId, page, sort, order);
    }

    //Get Testing Service Booking details by ID
    @GetMapping("/testing-service-bookings/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public TestingServiceBookingDTO
    getTestingServiceBookingDetailsById(@PathVariable int id) {

        return testingServiceBookingService.getTestingServiceBookingDetailsById(id);
    }

    //register testing service booking
    @PostMapping("/testing-service-bookings/register")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> registerTestingServiceBooking
    (@RequestBody TestingServiceBookingRegisterPayload payload) {

        testingServiceBookingService.createTestingServiceBooking(payload);
        return ResponseEntity.ok("Testing Service Booking registered successfully");
    }

    //Cancel consultation
    @PostMapping("/testing-service-bookings/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER ')")
    public ResponseEntity<String> cancelTestingServiceBooking
    (@PathVariable int id) {
        testingServiceBookingService.cancelTestingServiceBooking(id);
        return ResponseEntity.ok("Testing Service Booking cancelled successfully");
    }

    ////////////////////////////////// Manage Consultations ///////////////////////////////////


    //Get consultations by customer ID
    @GetMapping("/consultations/customer/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public Map<String, Object> getConsultationsByCustomerId
    (@PathVariable int customerId,
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "consultationId") String sort,
     @RequestParam(defaultValue = "asc") String order ) {

        return consultationService.getConsultationsByCustomerId(customerId, page, sort, order);
    }

    //Get consultation by ID
    @GetMapping("/consultations/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ConsultantConsultationDTO getConsultationById(@PathVariable int id) {
        return consultationService.getConsultationById(id);
    }

    //Get consultant schedule in a specific date for check
    @GetMapping("/consultations/consultant/{consultantId}/check-schedule")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ConsultantScheduleDTO> getConsultantScheduleByDate
    (@PathVariable int consultantId,
     @Parameter(example = "05/06/2025")
     @RequestParam("date")
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
     LocalDate date) {

        return ResponseEntity.ok(consultationService
                .getConsultantScheduleByDate(consultantId, date));
    }

    //register consultation
    @PostMapping("/consultations/register")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> registerConsulation
            (@RequestBody ConsultationRegisterPayload payload) {

        consultationService.registerConsultation(payload);
        return ResponseEntity.ok("Consultation registered successfully");
    }

    //register consultation
    @PostMapping("/consultations/{id}/evaluate")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> updateConsultationCommentAndRating
    (@PathVariable int id,
     @RequestBody ConsultationEvaluatePayload payload) {

        consultationService.updateConsultationCommentAndRating(id, payload);
        return ResponseEntity.ok("Consultation registered successfully");
    }

    //Cancel consultation
    @PostMapping("/consultations/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER ')")
    public ResponseEntity<String> cancelConsultation
    (@PathVariable int id) {
        consultationService.cancelConsultation(id);
        return ResponseEntity.ok("Consultation cancelled successfully");
    }
}
