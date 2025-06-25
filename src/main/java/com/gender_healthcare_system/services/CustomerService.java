package com.gender_healthcare_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.todo.ManagerCustomerDTO;

import com.gender_healthcare_system.dtos.user.CustomerPeriodDetailsDTO;
import com.gender_healthcare_system.entities.enu.Gender;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.iservices.ICustomerService;
import com.gender_healthcare_system.repositories.CustomerRepo;

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

    private final CustomerRepo customerRepo;

    @Override
    public LoginResponse getCustomerLoginDetails(int id) {
        return customerRepo.getCustomerLoginDetails(id);
    }

    public ManagerCustomerDTO getCustomerById(int id) {
        return customerRepo.getCustomerDetailsById(id)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));
    }

    public CustomerPeriodDetailsDTO getFemaleCustomerPeriodDetails
            (int customerId) throws JsonProcessingException {

        Customer customer = customerRepo.getCustomerById(customerId)
                .orElseThrow(() -> new AppException(400,
                        "No customer found with ID " + customerId));

        if (customer.getGender() == Gender.MALE){

            throw new AppException(400, "Cannot get period details for MALE customer");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return mapper.readValue
                (customer.getGenderSpecificDetails(), CustomerPeriodDetailsDTO.class);
    }

    public Map<String, Object> getAllCustomers(int page, String sortField, String sortOrder) {

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);

        if(sortOrder.equals("desc")){
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageRequest = PageRequest
                .of(page, itemSize, sort);


        Page<ManagerCustomerDTO> pageResult = customerRepo.getAllCustomers(pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Customers found");
        }

        List<ManagerCustomerDTO> customerList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();

        map.put("totalItems", pageResult.getTotalElements());
        map.put("customers", customerList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
    }
}
