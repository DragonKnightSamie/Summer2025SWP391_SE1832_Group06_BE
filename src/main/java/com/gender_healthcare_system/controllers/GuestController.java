package com.gender_healthcare_system.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gender_healthcare_system.payloads.CustomerPayload;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.gender_healthcare_system.payloads.LoginRequest;
import com.gender_healthcare_system.services.AccountService;
import com.gender_healthcare_system.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private AccountService accountService;

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
    public String login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {

            return jwtService.generateToken(loginRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

    // Guest login vào thành 1 role nào ó

}
