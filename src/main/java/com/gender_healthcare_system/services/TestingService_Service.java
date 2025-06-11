package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.TestingServiceDTO;
import com.gender_healthcare_system.entities.todo.TestingService;
import com.gender_healthcare_system.payloads.TestingServiceUpdatePayload;
import com.gender_healthcare_system.repositories.TestingServiceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TestingService_Service {

    private final TestingServiceRepo testingServiceRepo;

    // Create a new testing service
    @Transactional
    public TestingService createTestingService(TestingService testingService) {
        if (testingService == null) {
            throw new IllegalArgumentException("Testing Service cannot be null");
        }
        return testingServiceRepo.save(testingService);
    }


    public TestingServiceDTO getTestingServiceById(int id) {
        return testingServiceRepo.getTestingServiceById(id)
                .orElseThrow(() -> new RuntimeException("Testing Service not found with ID: " + id));
    }

    public List<TestingService> getAllTestingServices() {
        return testingServiceRepo.getAllTestingServices();
    }

    @Transactional
    public void updateTestingService(int id, TestingServiceUpdatePayload payload) {
        boolean serviceExists = testingServiceRepo.existsById(id);
        if (!serviceExists) {
            throw new RuntimeException("Testing Service not found with ID: " + id);
        }
        testingServiceRepo.updateTestingService(id, payload);

    }

    public void deleteTestingService(int id) {
        boolean serviceExists = testingServiceRepo.existsById(id);
        if (!serviceExists) {
            throw new RuntimeException("Testing Service not found with ID: " + id);
        }
        testingServiceRepo.deleteById(id);
    }


}
