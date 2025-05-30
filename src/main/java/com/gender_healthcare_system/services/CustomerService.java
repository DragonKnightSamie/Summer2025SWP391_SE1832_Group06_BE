package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.iservices.ICustomerService;
import com.gender_healthcare_system.repositories.CustomerRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    @Resource
    private CustomerRepo customerRepo;

    @Override
    public LoginResponse getCustomerLoginDetails(int id) {
        return customerRepo.getCustomerLoginDetails(id);
    }
}
