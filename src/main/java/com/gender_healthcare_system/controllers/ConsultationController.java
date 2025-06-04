package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.payloads.ConsultationConfirmPayload;
import com.gender_healthcare_system.services.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultation")
public class ConsultationController {

    /*@Autowired
    private ConsultationService consultationService;*/

    /*@PostMapping("/register")
    public String registerConsultation(@RequestBody ConsultationConfirmPayload payload) {
        consultationService.registerConsultation(payload);
        return "Consultation registered successfully";
    }*/

    /*@PostMapping("/cancel")
    public String cancelConsultation(@RequestBody ConsultationConfirmPayload payload) {
        consultationService.cancelConsultation(payload);
        return "Consultation cancelled successfully";
    }*/

    /*@PostMapping("/update")
    public String updateConsultation(@RequestBody ConsultationConfirmPayload payload) {
        consultationService.updateConsultation(payload);
        return "Consultation updated successfully";
    }*/

    //Confirm consultation (consultant)
   /* @PostMapping("/confirm")
    public String confirmConsultation(@RequestBody ConsultationConfirmPayload payload) {
        consultationService.confirmConsultation(payload);
        return "Consultation confirmed successfully";
    }*/




    //Get consultations by consultant ID
    /*@GetMapping("/consultant/{consultantId}")
    public List<Consultation> getConsultationsByConsultantId(@PathVariable int consultantId) {
        return consultationService.getConsultationsByConsultantId(consultantId);
    }*/
}
