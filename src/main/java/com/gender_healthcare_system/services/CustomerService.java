package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.dtos.ManagerCustomerDTO;

import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.iservices.ICustomerService;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.CustomerRepo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepo customerRepo;

    private final AccountRepo accountRepo;


    @Override
    public LoginResponse getCustomerLoginDetails(int id) {
        return customerRepo.getCustomerLoginDetails(id);
    }

    public ManagerCustomerDTO getCustomerById(int id) {
        return customerRepo.getCustomerDetailsById(id)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));
    }

    public List<ManagerCustomerDTO> getAllCustomers() {
        return customerRepo.getAllCustomers();
    }
}
