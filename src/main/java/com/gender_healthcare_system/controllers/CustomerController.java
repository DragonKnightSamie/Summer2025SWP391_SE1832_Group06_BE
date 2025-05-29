package com.gender_healthcare_system.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gender_healthcare_system.payloads.CustomerPayload;
import com.gender_healthcare_system.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public String register(@RequestBody CustomerPayload customerPayload) throws JsonProcessingException {
        accountService.createCustomerAccount(customerPayload);
        return "Customer registered successfully";
    }
}
