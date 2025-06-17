package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.todo.ConsultantConsultationDTO;
import com.gender_healthcare_system.dtos.todo.ConsultantDetailsDTO;
import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.login.LoginRequest;
import com.gender_healthcare_system.payloads.todo.CertificateUpdatePayload;
import com.gender_healthcare_system.payloads.todo.ConsultationCompletePayload;
import com.gender_healthcare_system.payloads.todo.ConsultationConfirmPayload;
import com.gender_healthcare_system.payloads.user.ConsultantUpdatePayload;
import com.gender_healthcare_system.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/consultant")
@AllArgsConstructor
public class ConsultantController {

    private final ConsultationService consultationService;
    
    private final ConsultantService consultantService;

    private final CertificateService certificateService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Invalid Username or Password");
        }

        boolean hasRole = authentication.getAuthorities()
                .stream()
                .anyMatch(auth ->
                        auth.getAuthority().equals("ROLE_CONSULTANT"));

        if (!hasRole) {
            throw new UsernameNotFoundException("Access denied for non-consultant user");
        }

        AccountInfoDetails account =
                (AccountInfoDetails) authentication.getPrincipal();
        int id = account.getId();

        LoginResponse loginDetails = consultantService
                .getConsultantLoginDetails(id);
        loginDetails.setUsername(loginRequest.getUsername());

        String jwtToken = jwtService.generateToken(loginRequest.getUsername());
        loginDetails.setToken(jwtToken);

        return loginDetails;
        //return jwtService.generateToken(loginRequest.getUsername());
    }



    ////////////////////////////////// Manage Profile ///////////////////////////////////


    //Consultant get profile infos
    @GetMapping("/profile/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ConsultantDetailsDTO>
    getConsultantProfile(@PathVariable int id) {

        ConsultantDetailsDTO consultantDetails = consultantService.getConsultantDetails(id);
        if (consultantDetails != null) {
            return ResponseEntity.ok(consultantDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Consultant update profile personal infos
    @PutMapping("profile/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<?> updateConsultantProfile
    (@PathVariable int id, @RequestBody ConsultantUpdatePayload payload) {

        consultantService.updateConsultantDetails(id, payload);
        return ResponseEntity.ok("Consultant profile updated successfully");
    }


    ////////////////////////////////// Manage Certificates ///////////////////////////////////


    //Consultant update certificate infos
    @PutMapping("certificates/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<?> updateConsultantCertificate
    (@PathVariable int id, @RequestBody CertificateUpdatePayload payload) {

        certificateService.updateConsultantCertificate(id, payload);
        return ResponseEntity.ok("Consultant certificate updated successfully");
    }


    ////////////////////////////////// Manage Consultations ///////////////////////////////////


    //Get all consultations by consultant ID
    @GetMapping("/consultations/consultantId/{id}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<Map<String, Object>>
    getConsultationsByConsultantId(@PathVariable int id,
                     @RequestParam(defaultValue = "0") int page,
                     @RequestParam(defaultValue = "consultationId") String sort,
                     @RequestParam(defaultValue = "asc") String order ) {

        return ResponseEntity.ok(consultationService
                .getConsultationsByConsultantId(id, page, sort, order));
    }

    //Get consultation by ID
    @GetMapping("/consultations/{id}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ConsultantConsultationDTO getConsultationById(@PathVariable int id) {
        return consultationService.getConsultationById(id);
    }

    //Confirm consultation
    /*@PostMapping("/consultations/confirm")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> confirmConsultation(@RequestBody ConsultationConfirmPayload payload) {
        consultationService.confirmConsultation(payload);
        return ResponseEntity.ok("Consultation confirmed successfully");
    }*/

    //Cancel consultation
    //Can consultant actually cancel consultation
    /*@PostMapping("/consultations/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> cancelConsultation
    (@PathVariable int id) {
        consultationService.cancelConsultation(id);
        return ResponseEntity.ok("Consultation cancelled successfully");
    }*/

    //Reschedule consultation
    @PostMapping("/consultations/{id}/reschedule/")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> reScheduleConsultation
    (@PathVariable int id, @RequestBody ConsultationConfirmPayload payload) {

        consultationService.reScheduleConsultation(id, payload);
        return ResponseEntity.ok("Consultation rescheduled successfully");
    }

    //Complete consultation
    @PostMapping("/consultations/complete")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> completeConsultation
    (@RequestBody ConsultationCompletePayload payload) {

        consultationService.completeConsultation(payload);
        return ResponseEntity.ok("Consultation rescheduled successfully");
    }
}
