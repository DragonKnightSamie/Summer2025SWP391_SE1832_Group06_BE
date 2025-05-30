package com.gender_healthcare_system.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.CustomerPayload;
import com.gender_healthcare_system.payloads.LoginRequest;
import com.gender_healthcare_system.services.AccountService;
import com.gender_healthcare_system.services.CustomerService;
import com.gender_healthcare_system.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

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

        boolean checkAuth = authentication
                .getAuthorities()
                .stream()
                .anyMatch(x -> x
                        .getAuthority().equals("ROLE_CUSTOMER"));

        if(!checkAuth){
            throw new UsernameNotFoundException("Invalid Username or Password");
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
}
