package com.gender_healthcare_system.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gender_healthcare_system.dtos.ConsultantConsultationDTO;
import com.gender_healthcare_system.dtos.ConsultationsDTO;
import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.ConsultationRegisterPayload;
import com.gender_healthcare_system.payloads.CustomerPayload;
import com.gender_healthcare_system.payloads.LoginRequest;
import com.gender_healthcare_system.services.AccountService;
import com.gender_healthcare_system.services.ConsultationService;
import com.gender_healthcare_system.services.CustomerService;
import com.gender_healthcare_system.services.JwtService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public String logout(@RequestBody String token) {
        jwtService.isTokenBlacklisted(token);
        return "Logout successful";
    }


    ////////////////////////////////// Manage Consultations ///////////////////////////////////


    //Get consultations by customer ID
    @GetMapping("/consultations/{customerId}")
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

    //register consultation
    @PostMapping("/consultations/register")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> registerConsulation
            (@RequestBody ConsultationRegisterPayload payload) {

        consultationService.registerConsultation(payload);
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
