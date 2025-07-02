package com.gender_healthcare_system.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.todo.*;
import com.gender_healthcare_system.dtos.user.ConsultantDTO;
import com.gender_healthcare_system.dtos.user.CustomerPeriodDetailsDTO;
import com.gender_healthcare_system.dtos.user.CustomerDTO;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.MenstrualCreatePayload;
import com.gender_healthcare_system.payloads.login.LoginRequest;
import com.gender_healthcare_system.payloads.todo.EvaluatePayload;
import com.gender_healthcare_system.payloads.todo.ConsultationRegisterPayload;
import com.gender_healthcare_system.payloads.todo.MenstrualCycleUpdatePayload;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingRegisterPayload;
import com.gender_healthcare_system.payloads.user.CustomerPayload;
import com.gender_healthcare_system.payloads.user.CustomerUpdatePayload;
import com.gender_healthcare_system.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.List;
import java.util.Map;

@Tag(name = "Customer APIs", description = "APIs for managing customer functionalities")
@RestController
@RequestMapping("/api/v1/customer")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final ConsultationService consultationService;

    private final JwtService jwtService;

    private final AccountService accountService;

    private final AuthenticationManager authenticationManager;

    private final TestingServiceBookingService testingServiceBookingService;

    private final TestingService_Service testingService_service;

    private final PriceListService priceListService;

    private final ConsultantService consultantService;

    private final CertificateService certificateService;

    private final MomoPaymentService momoPaymentService;

    @Operation(
            summary = "Register a new customer",
            description = "Allows a new customer to register by providing necessary details."
    )
    @PostMapping("/register")
    public String register(@RequestBody @Valid CustomerPayload customerPayload)
            throws JsonProcessingException {
        accountService.createCustomerAccount(customerPayload);
        return "Customer registered successfully";
    }

    @Operation(
            summary = "Customer login",
            description = "Allows a customer to log in and receive a JWT token for authentication."
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid Username or Password");
        }

        boolean hasRole = authentication
                .getAuthorities()
                .stream()
                .anyMatch(x -> x
                        .getAuthority().equals("ROLE_CUSTOMER"));

        if (!hasRole) {
            throw new UsernameNotFoundException
                    ("Access denied for non-customer user");
        }

        AccountInfoDetails account = (AccountInfoDetails) authentication.getPrincipal();
        int id = account.getId();

        LoginResponse loginDetails = customerService.getCustomerLoginDetails(id);
        //loginDetails.setUsername(loginRequest.getUsername());

        String jwtToken = jwtService.generateToken(id, loginRequest.getUsername(),
                account.getRolename(), loginDetails.getFullname(), loginDetails.getEmail());
        //loginDetails.setToken(jwtToken);

        return ResponseEntity.ok(jwtToken);
    }

    @Operation(
            summary = "Get Customer Period Details",
            description = "Get menstrual cycle details for a female customer"
    )
    //get period details for a female customers
    @GetMapping("/female/period-details/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<CustomerPeriodDetailsDTO>
    getPeriodDetailsForFemaleCustomer
            (@PathVariable int customerId) throws JsonProcessingException {

        return ResponseEntity.ok(customerService.getFemaleCustomerPeriodDetails(customerId));
    }


    /// /////////////////////////////// Manage Customer Profile /////////////////////////////////

    @Operation(
            summary = "Get Customer Profile",
            description = "Get profile information of a customer by their ID."
    )
    //Customer get profile infos
    @GetMapping("/profile/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<CustomerDTO>
    getCustomerProfile(@PathVariable int id) throws JsonProcessingException {

        return ResponseEntity.ok(customerService.getCustomerById(id));

    }

    @Operation(
            summary = "Update Customer Profile",
            description = "Update profile information of a customer by their ID."
    )
    //Customer update profile infos
    @PutMapping("profile/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<?> updateConsultantProfile
            (@PathVariable int id, @RequestBody @Valid CustomerUpdatePayload payload)
            throws JsonProcessingException {

        customerService.updateCustomerDetails(id, payload);
        return ResponseEntity.ok("Customer profile updated successfully");
    }

    /// /////////////////////////////// Manage Momo Payment /////////////////////////////////

    //create payment for consultation registering
    /*@PostMapping("/payment-transaction/create")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> createPaymentRequest
    (@RequestBody @Valid MomoPaymentPayload payload) throws Exception {

        return ResponseEntity.ok(momoPaymentService.createMomoPaymentRequest(payload));
    }*/


    //Get customer payment info
    /*@GetMapping("/payment-transaction/check-error")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> handleCallback(
            @RequestParam String transactionId,
            @RequestParam String resultCode
    ) {
        if ("0".equals(resultCode)) {
            // payment success
            return ResponseEntity.ok(
                    "Payment successful for transactionId " + transactionId);
        } else {
            // payment failed
            return ResponseEntity.status(400).body("Payment failed with code: " + resultCode);
        }
    }*/


    /// /////////////////////////////// Manage Testing Service Bookings /////////////////////////

    @Operation(
            summary = "Get all testing services",
            description = "Retrieve a paginated list of all available testing services for customers."
    )
    //get all testing services
    @GetMapping("/testing-services/list")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Map<String, Object>> getAllTestingServicesForCustomer
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "serviceId") String sort,
     @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(testingService_service
                .getAllTestingServicesForCustomer(page, sort, order));
    }

    @Operation(
            summary = "Get Testing Service by ID",
            description = "Retrieve details of a specific testing service by its ID."
    )
    //get price list for a testing service
    @GetMapping("/testing-services/price-list/{serviceId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<PriceListDTO>> getPriceListForTestingService
            (@PathVariable int serviceId) {

        return ResponseEntity.ok(priceListService.getPriceListForTestingService(serviceId));
    }


    @Operation(
            summary = "Get Testing Service Booking by Customer ID",
            description = "Retrieve all testing service bookings made by a specific customer."
    )
    //Get Service bookings by customer ID
    @GetMapping("/testing-service-bookings/customer/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Map<String, Object>> getTestingServiceBookingsByCustomerId
            (@PathVariable int customerId,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "serviceBookingId") String sort,
             @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(testingServiceBookingService
                .getAllTestingServiceBookingsByCustomerId(customerId, page, sort, order));
    }

    @Operation(
            summary = "Get Testing Service Booking Details by ID",
            description = "Retrieve details of a specific testing service booking by its ID."
    )
    //Get Testing Service Booking details by ID
    @GetMapping("/testing-service-bookings/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<TestingServiceBookingDTO>
    getTestingServiceBookingDetailsById(@PathVariable int id) throws JsonProcessingException {

        return ResponseEntity.ok(testingServiceBookingService.getTestingServiceBookingDetailsById(id));
    }

    @Operation(
            summary = "Get Testing Service Booking by ID",
            description = "Retrieve details of a specific testing service booking by its ID."
    )
    //register testing service booking
    @PostMapping("/testing-service-bookings/register")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> registerTestingServiceBooking
            (@RequestBody @Valid TestingServiceBookingRegisterPayload payload) {

        testingServiceBookingService.createTestingServiceBooking(payload);
        return ResponseEntity.ok("Testing Service Booking registered successfully");
    }

    @Operation(
            summary = "Evaluate Testing Service Booking",
            description = "Allows a customer to review and comment on a testing service booking."
    )
    //Review and comment Testing Service Booking
    @PutMapping("/testing-service-bookings/{id}/evaluate")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> updateTestingServiceBookingCommentAndRating
            (@PathVariable int id,
             @RequestBody @Valid EvaluatePayload payload) {

        testingServiceBookingService
                .updateTestingServiceBookingCommentAndRating(id, payload);
        return ResponseEntity.ok(
                "Testing Service Booking rating and comment updated successfully");
    }

    @Operation(
            summary = "Cancel Testing Service Booking",
            description = "Allows a customer to cancel a previously booked testing service."
    )
    //Cancel testing service booking
    @PutMapping("/testing-service-bookings/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> cancelTestingServiceBooking
            (@PathVariable int id) {
        testingServiceBookingService.cancelTestingServiceBooking(id);
        return ResponseEntity.ok("Testing Service Booking cancelled successfully");
    }

    /// /////////////////////////////// Manage Consultations ///////////////////////////////////

    @Operation(
            summary = "Get Customer Profile",
            description = "Get profile information of a customer by their ID."
    )
    //Get all consultant
    //lấy danh sách consltant cho customer chon và xem thông tin
    @GetMapping("/consultant-list/")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<ConsultantDTO>> getAllConsultantForCustomer() {
        return ResponseEntity.ok(consultantService.getAllConsultantsForCustomer());
    }

    @Operation(
            summary = "Get Consultant Certificates",
            description = "Retrieve all certificates of a consultant by their ID."
    )
    //get consultant certificates by ID
    @GetMapping("/consultant-list/certificates/{consultantId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<CertificateDTO>> getConsultantCertificates
            (@PathVariable int consultantId) {
        return ResponseEntity.ok(certificateService.getConsultantCertificates(consultantId));
    }

    @Operation(
            summary = "Get Consultations by Customer ID",
            description = "Retrieve all consultations made by a specific customer with pagination, sorting, and ordering options."
    )
    //Get consultations by customer ID
    @GetMapping("/consultations/customer/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Map<String, Object>> getConsultationsByCustomerId
            (@PathVariable int customerId,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "consultationId") String sort,
             @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(consultationService.getConsultationsByCustomerId(customerId, page, sort, order));
    }

    @Operation(
            summary = "Get Consultation by ID",
            description = "Retrieve details of a specific consultation by its ID for a customer."
    )
    //Get consultation by ID
    @GetMapping("/consultations/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ConsultationDTO>
    getConsultationById(@PathVariable int id) {
        return ResponseEntity.ok(consultationService.getConsultationByIdForCustomer(id));
    }

    @Operation(
            summary = "Get Consultant Schedule by Date",
            description = "Retrieve the schedule of a consultant for a specific date."
    )
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

    @Operation(
            summary = "Register Consultation",
            description = "Allows a customer to register a new consultation with a consultant."
    )
    //register consultation
    @PostMapping("/consultations/register")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> registerConsultation
            (@RequestBody @Valid ConsultationRegisterPayload payload) {

        consultationService.registerConsultation(payload);
        return ResponseEntity.ok("Consultation registered successfully");
    }

    @Operation(
            summary = "Evaluate Consultation",
            description = "Allows a customer to review and comment on a consultation."
    )
    //Review and comment consultation
    @PutMapping("/consultations/{id}/evaluate")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> updateConsultationCommentAndRating
            (@PathVariable int id,
             @RequestBody @Valid EvaluatePayload payload) {

        consultationService.updateConsultationCommentAndRating(id, payload);
        return ResponseEntity.ok(
                "Consultation rating and comment updated successfully");
    }

    @Operation(
            summary = "Cancel Consultation",
            description = "Allows a customer to cancel a previously booked consultation."
    )
    //Cancel consultation
    @PutMapping("/consultations/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER ')")
    public ResponseEntity<String> cancelConsultation
            (@PathVariable int id) {
        consultationService.cancelConsultation(id);
        return ResponseEntity.ok("Consultation cancelled successfully");
    }

    /// ////////////Menstrual Cycle Management /////////////////////////
    private final MenstrualCycleService menstrualCycleService;

    @Operation(
            summary = "Create menstrual cycle",
            description = "Create a new menstrual cycle for a customer"
    )
    @PostMapping("/menstrual-cycles/create")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<MenstrualCycleDTO> createCycle(@RequestBody @Valid MenstrualCreatePayload payload) {
        MenstrualCycleDTO createdCycle = menstrualCycleService.createCycle(payload);
        return ResponseEntity.ok(createdCycle);
    }

    @Operation(
            summary = "Get cycles by customer ID",
            description = "Retrieve all cycles for a specific customer"
    )
    @GetMapping("/menstrual-cycles/customer/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<MenstrualCycleDTO>> getCyclesByCustomerId(@PathVariable int customerId) {
        List<MenstrualCycleDTO> cycles = menstrualCycleService.getCyclesByCustomerId(customerId);
        return ResponseEntity.ok(cycles);
    }

    @Operation(
            summary = "Update menstrual cycle",
            description = "Update a menstrual cycle by its ID"
    )
    @PutMapping("/menstrual-cycles/update/{cycleId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> updateCycleById(
            @PathVariable Long cycleId,
            @RequestBody @Valid MenstrualCycleUpdatePayload payload
    ) {
        menstrualCycleService.updateCycleById(cycleId, payload);
        return ResponseEntity.ok("Updated menstrual cycle successfully");
    }
}



