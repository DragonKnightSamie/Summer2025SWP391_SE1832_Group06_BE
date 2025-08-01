package com.gender_healthcare_system.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gender_healthcare_system.dtos.todo.MenstrualCycleDTO;
import com.gender_healthcare_system.entities.todo.MenstrualCycle;
import com.gender_healthcare_system.entities.user.Account;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.todo.MenstrualCreatePayload;
import com.gender_healthcare_system.payloads.todo.MenstrualCycleUpdatePayload;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.MenstrualCycleRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenstrualCycleService {

    private final MenstrualCycleRepo menstrualCycleRepo;
    private final AccountRepo accountRepo;

    public MenstrualCycleDTO createCycle(MenstrualCreatePayload payload, int customerId) {
        Account customer = accountRepo.findById(customerId)
                .orElseThrow(() -> new AppException(404, "Customer not found"));

        MenstrualCycle cycle = new MenstrualCycle();
        cycle.setStartDate(payload.getStartDate());
        cycle.setEndDate(payload.getEndDate());
        cycle.setCycleLength(payload.getCycleLength());
        cycle.setIsTrackingEnabled(payload.getIsTrackingEnabled());
        cycle.setCreatedAt(LocalDateTime.now());
        cycle.setUpdatedAt(LocalDateTime.now());
        cycle.setCustomer(customer);
        cycle.setFlowVolume(payload.getFlowVolume());
        cycle.setOvulationDate(payload.getOvulationDate());
        cycle.setWeight(payload.getWeight());

        MenstrualCycle saved = menstrualCycleRepo.save(cycle);

        return new MenstrualCycleDTO(
                saved.getCycleId(),
                saved.getStartDate(),
                saved.getCycleLength(),
                saved.getIsTrackingEnabled(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getEndDate(),
                saved.getSeverity(),
                saved.getStatus(),
                saved.getNote(),
                saved.getFlowVolume(),
                saved.getOvulationDate(),
                saved.getWeight()
        );
    }

    public List<MenstrualCycleDTO> getCyclesByCustomerId(int customerId) {
        List<MenstrualCycleDTO> cycles = menstrualCycleRepo.getCyclesByCustomerId(customerId);
        if (cycles.isEmpty()) {
            throw new AppException(404, "No menstrual cycles found for this customer");
        }
        return cycles;
    }

    @Transactional
    public void updateCycleById(Integer cycleId, MenstrualCycleUpdatePayload payload) {
        boolean exists = menstrualCycleRepo.existsById(cycleId);
        if (!exists) {
            throw new AppException(404, "Menstrual cycle not found");
        }
        // Fetch the entity, update all fields including endDate, and save
        MenstrualCycle cycle = menstrualCycleRepo.findById(cycleId)
                .orElseThrow(() -> new AppException(404, "Menstrual cycle not found"));
        cycle.setStartDate(payload.getStartDate());
        cycle.setEndDate(payload.getEndDate());
        cycle.setCycleLength(payload.getCycleLength());
        cycle.setIsTrackingEnabled(payload.getIsTrackingEnabled());
        cycle.setSeverity(payload.getSeverity());
        cycle.setStatus(payload.getStatus());
        cycle.setNote(payload.getNote());
        cycle.setUpdatedAt(java.time.LocalDateTime.now());
        cycle.setFlowVolume(payload.getFlowVolume());
        cycle.setOvulationDate(payload.getOvulationDate());
        cycle.setWeight(payload.getWeight());
        menstrualCycleRepo.save(cycle);
    }
}
