package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.todo.SymptomDTO;
import com.gender_healthcare_system.entities.todo.Symptom;
import com.gender_healthcare_system.entities.user.Account;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.todo.SymptomCreatePayload;
import com.gender_healthcare_system.payloads.todo.SymptomUpdatePayload;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.SymptomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SymptomService {

    private final SymptomRepo symptomRepo;
    private final AccountRepo accountRepo;

    public SymptomDTO createSymptom(SymptomCreatePayload payload) {
        Account customer = accountRepo.findById(payload.getCustomerId())
                .orElseThrow(() -> new AppException(404, "Customer not found"));

        Symptom symptom = new Symptom();
        symptom.setName(payload.getName());
        symptom.setDescription(payload.getDescription());
        symptom.setRecordedAt(LocalDateTime.now());
        symptom.setCustomer(customer);

        Symptom saved = symptomRepo.save(symptom);
        return new SymptomDTO(saved.getSymptomId(), saved.getName(),
                saved.getDescription(), saved.getRecordedAt(), saved.getCustomer());
    }

    public List<SymptomDTO> getSymptomsByCustomerId(int customerId) {
        return symptomRepo.getSymptomsByCustomerId(customerId);
    }

    public void updateSymptom(Long id, SymptomUpdatePayload payload) {
        Symptom existing = symptomRepo.findById(id)
                .orElseThrow(() -> new AppException(404, "Symptom not found"));

        existing.setName(payload.getName());
        existing.setDescription(payload.getDescription());
        existing.setRecordedAt(LocalDateTime.now());

        symptomRepo.save(existing);
    }

    public void deleteSymptom(Long id) {
        if (!symptomRepo.existsById(id)) {
            throw new AppException(404, "Symptom not found");
        }
        symptomRepo.deleteById(id);
    }
}
