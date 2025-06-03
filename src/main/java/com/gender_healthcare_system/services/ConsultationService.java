package com.gender_healthcare_system.services;

import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.entities.user.Account;
import com.gender_healthcare_system.entities.user.Consultant;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.payloads.ConsultationPayload;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.ConsultantRepo;
import com.gender_healthcare_system.repositories.ConsultationRepo;
import com.gender_healthcare_system.repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ConsultantRepo consultantRepo;

    @Autowired
    private ConsultationRepo consultationRepo;

    //getConsultationById
    public Consultation getConsultationById(int id) {
        return consultationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
    }

    //getConsultationByCustomerId
    public List<Consultation> getConsultationsByCustomerId(int customerId) {
        return consultationRepo.findByCustomer_CustomerId(customerId);
    }

    //getConsultationByConsultantId
    public List<Consultation> getConsultationsByConsultantId(int consultantId) {
        return consultationRepo.findByConsultant_ConsultantId(consultantId);
    }


    //register
    public Consultation registerConsultation(ConsultationPayload payload) {
        Customer customer = customerRepo.findById(payload.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Consultant consultant = consultantRepo.findById(payload.getConsultantId())
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        Consultation consultation = new Consultation();
        consultation.setCreatedAt(LocalDateTime.now());
        consultation.setExpectedStartTime(payload.getExpectedStartTime());
        consultation.setExpectedEndTime(payload.getExpectedEndTime());
        consultation.setStatus(ConsultationStatus.PENDING);
        consultation.setCustomer(customer);
        consultation.setConsultant(consultant);

        return consultationRepo.save(consultation);

    }

    //confirm consultation : consultant xác nhận lịch hẹn
    public void confirmConsultation(ConsultationPayload payload) {
        Consultation consultation = consultationRepo.findById(payload.getConsultationId())
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        if (consultation.getStatus() != ConsultationStatus.PENDING) {
            throw new RuntimeException("Only pending consultations can be approved");
        }

        consultation.setStatus(ConsultationStatus.CONFIRMED);
        consultation.setExpectedStartTime(payload.getExpectedStartTime());
        consultation.setExpectedEndTime(payload.getExpectedEndTime());

        consultationRepo.save(consultation);
    }

    public void cancelConsultation(ConsultationPayload payload) {
        Consultation consultation = consultationRepo.findById(payload.getConsultationId())
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        if (consultation.getStatus() == ConsultationStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed consultation");
        }

        consultation.setStatus(ConsultationStatus.CANCELLED);
        consultationRepo.save(consultation);
    }

    public void updateConsultation(ConsultationPayload payload) {
        Consultation consultation = consultationRepo.findById(payload.getConsultationId())
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        if (consultation.getStatus() == ConsultationStatus.COMPLETED || consultation.getStatus() == ConsultationStatus.CANCELLED) {
            throw new RuntimeException("Cannot update a completed or cancelled consultation");
        }

        consultation.setExpectedStartTime(payload.getExpectedStartTime());
        consultation.setExpectedEndTime(payload.getExpectedEndTime());

        consultationRepo.save(consultation);
    }
}
