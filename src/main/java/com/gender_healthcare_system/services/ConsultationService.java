package com.gender_healthcare_system.services;

import com.gender_healthcare_system.entities.todo.Consultation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationService consultationService;

    public Consultation createConsultation(Consultation consultation) {
        return consultationService.createConsultation(consultation);
    }
}
