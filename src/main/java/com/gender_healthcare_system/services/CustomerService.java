package com.gender_healthcare_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.user.CustomerDTO;

import com.gender_healthcare_system.dtos.user.CustomerPeriodDetailsDTO;
import com.gender_healthcare_system.entities.enu.Gender;
import com.gender_healthcare_system.entities.user.Account;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.iservices.ICustomerService;
import com.gender_healthcare_system.payloads.user.CustomerUpdatePayload;
import com.gender_healthcare_system.repositories.AccountRepo;

import com.gender_healthcare_system.utils.UtilFunctions;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService {

    private final AccountRepo accountRepo;

    @Override
    public LoginResponse getCustomerLoginDetails(int id) {
        return accountRepo.getCustomerLoginDetails(id);
    }

    public CustomerDTO getCustomerById(int id) throws JsonProcessingException {
        CustomerDTO customerDetails = accountRepo.getCustomerDetailsById(id)
                .orElseThrow(() -> new AppException(404,
                        "Customer not found with ID " + id));

        customerDetails.setPassword(null);
        customerDetails.setStatus(null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        CustomerPeriodDetailsDTO periodDetails = null;

        if(customerDetails.getGender() == Gender.FEMALE
                && customerDetails.getGenderSpecificDetails() != null){

            periodDetails = mapper.readValue
                    (customerDetails.getGenderSpecificDetails(),
                            CustomerPeriodDetailsDTO.class);
        }

        customerDetails.setPeriodDetails(periodDetails);
        customerDetails.setGenderSpecificDetails(null);

        return customerDetails;

    }

    public CustomerDTO getCustomerForManagerById(int id) throws JsonProcessingException {
        CustomerDTO customerDetails = accountRepo.getCustomerDetailsById(id)
                .orElseThrow(() -> new AppException(404,
                        "Customer not found with ID " + id));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        CustomerPeriodDetailsDTO periodDetails = null;

        if(customerDetails.getGender() == Gender.FEMALE
                && customerDetails.getGenderSpecificDetails() != null){

            periodDetails = mapper.readValue
                    (customerDetails.getGenderSpecificDetails(),
                            CustomerPeriodDetailsDTO.class);
        }

        customerDetails.setPeriodDetails(periodDetails);
        customerDetails.setGenderSpecificDetails(null);

        return customerDetails;
    }

    public CustomerPeriodDetailsDTO getFemaleCustomerPeriodDetails
            (int customerId) throws JsonProcessingException {

        Account account = accountRepo.findById(customerId)
                .orElseThrow(() -> new AppException(400,
                        "No customer found with ID " + customerId));

        if (account.getGender() == Gender.MALE){

            throw new AppException(400, "Cannot get period details for MALE customer");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return mapper.readValue
                (account.getGenderSpecificDetails(), CustomerPeriodDetailsDTO.class);
    }

    public Map<String, Object> getAllCustomers(int page, String sortField, String sortOrder) {

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);

        if(sortOrder.equals("desc")){
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageRequest = PageRequest
                .of(page, itemSize, sort);


        Page<CustomerDTO> pageResult = accountRepo.getAllCustomers(pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Customers found");
        }

        List<CustomerDTO> customerList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();

        map.put("totalItems", pageResult.getTotalElements());
        map.put("customers", customerList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
    }

    @Transactional
    public void updateCustomerDetails
            (int id, CustomerUpdatePayload payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Account account = accountRepo.findById(id)
                .orElseThrow(() -> new AppException(404, "No Customer found with ID " + id));

        UtilFunctions.validatePeriodDetails
                (account.getGender(), payload.getGenderSpecificDetails());

        String GenderSpecificDetails = null;
        if(account.getGender() == Gender.FEMALE) {

            GenderSpecificDetails = mapper.writeValueAsString(
                    payload.getGenderSpecificDetails());
        }

        accountRepo.updateCustomerById(id, payload);
    }
}
