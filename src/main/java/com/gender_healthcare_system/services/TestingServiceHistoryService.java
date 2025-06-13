package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.TestingServiceHistoryDTO;
import com.gender_healthcare_system.entities.todo.TestingServiceHistory;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.TestingServiceHistoryPayload;
import com.gender_healthcare_system.repositories.TestingServiceHistoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TestingServiceHistoryService {

    private final TestingServiceHistoryRepo testingServiceHistoryRepo;

    // Get TestingServiceHistoryDTO by id
    public TestingServiceHistoryDTO getTestingServiceHistoryDTOById(int id) {
        return testingServiceHistoryRepo.getTestingServiceHistoryById(id)
                .orElseThrow(() -> new AppException(404, "Testing Service History not found with ID: " + id));
    }

    // Get TestingServiceHistory entity by id
    public TestingServiceHistory getTestingServiceHistoryById(int id) {
        return testingServiceHistoryRepo.getTestingServiceHistory(id)
                .orElseThrow(() -> new AppException(404, "Testing Service History not found with ID: " + id));
    }

    // Get all TestingServiceHistories with pagination
    public Map<String, Object> getAllTestingServiceHistories(int page, String sortField, String sortOrder) {
        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);
        if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageable = PageRequest.of(page, itemSize, sort);
        Page<TestingServiceHistory> pageResult = testingServiceHistoryRepo.findAll(pageable);

        if (!pageResult.hasContent()) {
            throw new AppException(404, "No Testing Service Histories found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", pageResult.getTotalElements());
        response.put("testingServiceHistories", pageResult.getContent());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("currentPage", pageResult.getNumber());

        return response;
    }

    // Update
    @Transactional
    public void updateTestingServiceHistory(int id, TestingServiceHistoryPayload payload) {
        boolean exists = testingServiceHistoryRepo.existsById(id);
        if (!exists) {
            throw new AppException(404, "Testing Service History not found with ID: " + id);
        }
        testingServiceHistoryRepo.updateTestingServiceHistory(id, payload);
    }

    // Delete
    @Transactional
    public void deleteTestingServiceHistory(int id) {
        boolean existsHistory = testingServiceHistoryRepo.existsById(id);
        if (!existsHistory) {
            throw new AppException(404, "Testing Service History not found with ID: " + id);
        }
        testingServiceHistoryRepo.deleteTestingServiceHistory(id);
    }
}
