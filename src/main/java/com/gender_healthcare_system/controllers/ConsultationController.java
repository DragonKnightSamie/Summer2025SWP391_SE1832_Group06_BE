package com.gender_healthcare_system.controllers;

import org.springframework.web.bind.annotation.*;

/*@RestController
@RequestMapping("/api/consultation")*/
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
