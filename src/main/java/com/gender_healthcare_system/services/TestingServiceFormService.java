package com.gender_healthcare_system.services;

import com.gender_healthcare_system.payloads.TestingServiceFormUpdatePayload;
import com.gender_healthcare_system.repositories.TestingServiceFormRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TestingServiceFormService {

    private final TestingServiceFormRepo testingServiceFormRepo;

    //updateTestingServiceFormById
    public void updateTestingServiceFormById(int id, TestingServiceFormUpdatePayload payload){
        boolean formExists = testingServiceFormRepo.existsById(id);
        if (!formExists) {
            throw new RuntimeException("Testing Service Form not found with ID: " + id);
        }
        testingServiceFormRepo.updateContentById(id, payload);
    }

    //deleteTestingServiceFormById
    public void deleteTestingServiceFormById(int id){
        boolean formExists = testingServiceFormRepo.existsById(id);
        if (!formExists) {
            throw new RuntimeException("Testing Service Form not found with ID: " + id);
        }
        testingServiceFormRepo.deleteByServiceFormId(id);
    }
}
