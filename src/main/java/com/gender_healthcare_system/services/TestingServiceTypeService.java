package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.todo.TestingServiceResultDTO;
import com.gender_healthcare_system.dtos.todo.TestingServiceTypeDTO;
import com.gender_healthcare_system.dtos.todo.TestingServiceTypeDetailsDTO;
import com.gender_healthcare_system.entities.todo.*;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.todo.TestingServiceResultPayload;
import com.gender_healthcare_system.payloads.todo.TestingServiceTypeRegisterPayload;
import com.gender_healthcare_system.payloads.todo.TestingServiceTypeUpdatePayload;
import com.gender_healthcare_system.repositories.TestingServiceResultRepo;
import com.gender_healthcare_system.repositories.TestingServiceTypeRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TestingServiceTypeService {

    private final TestingServiceTypeRepo testingServiceTypeRepo;

    private final TestingServiceResultRepo testingServiceResultRepo;


    public TestingServiceTypeDetailsDTO getTestingServiceById(int id) {
        TestingServiceTypeDetailsDTO serviceTypeDetails =
                testingServiceTypeRepo.getTestingServiceDetailsById(id)
                .orElseThrow(() -> new AppException
                        (404,"Testing Service type not found with ID: " + id));

        List<TestingServiceResultDTO> serviceTypeResultList =
                testingServiceResultRepo.getAllServiceResultsByTypeId(id);

        serviceTypeDetails.setServiceResultList(serviceTypeResultList);
        return serviceTypeDetails;
    }

    public Map<String, Object> getAllTestingServiceTypes(int page, String sortField, String sortOrder) {

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);

        if(sortOrder.equals("desc")){
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageRequest = PageRequest
                .of(page, itemSize, sort);


        Page<TestingServiceTypeDTO> pageResult =
                testingServiceTypeRepo.getAllTestingServiceTypes(pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Testing Service Types found");
        }

        List<TestingServiceTypeDTO> serviceTypeList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();

        map.put("totalItems", pageResult.getTotalElements());
        map.put("serviceTypes", serviceTypeList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
    }

    // Create a new testing service type
    public void createTestingServiceType
    (TestingServiceTypeRegisterPayload payload) {

        TestingServiceType newServiceType = new TestingServiceType();

        newServiceType.setServiceTypeName(payload.getServiceTypeName());
        newServiceType.setTitle(payload.getTitle());
        newServiceType.setContent(payload.getContent());
        newServiceType.setCreatedAt(LocalDateTime.now());


        for(TestingServiceResultPayload item: payload.getServiceResultList()) {

            TestingServiceResult resultItem = new TestingServiceResult();

            resultItem.setTitle(item.getTitle());
            resultItem.setDescription(item.getDescription());

            newServiceType.addResult(resultItem);

        }

        /*if (testingService == null) {
            throw new IllegalArgumentException("Testing Service cannot be null");
        }*/

        testingServiceTypeRepo.saveAndFlush(newServiceType);
    }

    @Transactional
    public void updateTestingServiceType
            (int id, TestingServiceTypeUpdatePayload payload) {
        boolean serviceTypeExists = testingServiceTypeRepo.existsById(id);
        if (!serviceTypeExists) {
            throw new AppException(404, "Testing Service type not found with ID: " + id);
        }

        testingServiceTypeRepo.updateTestingServiceType(id, payload);

    }

    @Transactional
    public void deleteTestingServiceType(int id) {
        boolean serviceExists = testingServiceTypeRepo.existsById(id);
        if (!serviceExists) {
            throw new AppException(404, "Testing Service type not found with ID: " + id);
        }

        testingServiceResultRepo.deleteTestingServiceResultByServiceTypeId(id);
        testingServiceTypeRepo.deleteTestingServiceTypeById(id);
    }
}
