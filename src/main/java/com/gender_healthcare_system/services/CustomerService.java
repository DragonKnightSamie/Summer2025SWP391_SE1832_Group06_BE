package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.dtos.ManagerCustomerDTO;

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
