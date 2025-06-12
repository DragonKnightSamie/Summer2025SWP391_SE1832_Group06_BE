package com.gender_healthcare_system.services;

import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.repositories.TestingServiceFormRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TestingServiceFormService {

    private final TestingServiceFormRepo testingServiceFormRepo;

    //updateTestingServiceFormById
    @Transactional
    public void updateTestingServiceFormById(int id, String newContent){
        boolean formExists = testingServiceFormRepo.existsById(id);
        if (!formExists) {
            throw new AppException(404, "Testing Service Form not found with ID: " + id);
        }
        testingServiceFormRepo.updateContentById(id, newContent);
    }

    //deleteTestingServiceFormById
    public void deleteTestingServiceFormById(int id){
        boolean formExists = testingServiceFormRepo.existsById(id);
        if (!formExists) {
            throw new AppException(404, "Testing Service Form not found with ID: " + id);
        }
        testingServiceFormRepo.deleteByServiceFormId(id);
    }
}
