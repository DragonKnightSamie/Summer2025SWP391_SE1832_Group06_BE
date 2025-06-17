package com.gender_healthcare_system.services;

import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.todo.TestingServiceResultPayload;
import com.gender_healthcare_system.repositories.TestingServiceResultRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TestingServiceResultService {

    private final TestingServiceResultRepo testingServiceResultRepo;

    @Transactional
    public void updateTestingServiceResult
            (int id, TestingServiceResultPayload payload) {
        boolean serviceResultExists = testingServiceResultRepo.existsById(id);
        if (!serviceResultExists) {
            throw new AppException(404, "Testing Service result not found with ID: " + id);
        }

        testingServiceResultRepo.updateTestingServiceResult(id, payload);

    }


    public void deleteTestingServiceResult(int id) {
        boolean serviceResultExists = testingServiceResultRepo.existsById(id);
        if (!serviceResultExists) {
            throw new AppException(404, "Testing Service result not found with ID: " + id);
        }
        testingServiceResultRepo.deleteTestingServiceResultById(id);
    }
}
