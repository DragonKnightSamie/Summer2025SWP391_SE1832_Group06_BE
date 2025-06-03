package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.payloads.ConsultationPayload;
import com.gender_healthcare_system.services.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultation")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping("/register")
    public String registerConsultation(@RequestBody ConsultationPayload payload) {
        consultationService.registerConsultation(payload);
        return "Consultation registered successfully";
    }

    @PostMapping("/cancel")
    public String cancelConsultation(@RequestBody ConsultationPayload payload) {
        consultationService.cancelConsultation(payload);
        return "Consultation cancelled successfully";
    }

    @PostMapping("/update")
    public String updateConsultation(@RequestBody ConsultationPayload payload) {
        consultationService.updateConsultation(payload);
        return "Consultation updated successfully";
    }

    //Confirm consultation (consultant)
    @PostMapping("/confirm")
    public String confirmConsultation(@RequestBody ConsultationPayload payload) {
        consultationService.confirmConsultation(payload);
        return "Consultation confirmed successfully";
    }

    // 5. Get consultation by ID
    @GetMapping("/{id}")
    public Consultation getConsultationById(@PathVariable int id) {
        return consultationService.getConsultationById(id);
    }

    //Get consultations by customer ID
    @GetMapping("/customer/{customerId}")
    public List<Consultation> getConsultationsByCustomerId(@PathVariable int customerId) {
        return consultationService.getConsultationsByCustomerId(customerId);
    }

    //Get consultations by consultant ID
    @GetMapping("/consultant/{consultantId}")
    public List<Consultation> getConsultationsByConsultantId(@PathVariable int consultantId) {
        return consultationService.getConsultationsByConsultantId(consultantId);
    }
}
