package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.ConsultantConsultationDTO;
import com.gender_healthcare_system.dtos.ConsultationsDTO;
import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.ConsultationCompletePayload;
import com.gender_healthcare_system.payloads.ConsultationConfirmPayload;
import com.gender_healthcare_system.payloads.ConsultationRegisterPayload;
import com.gender_healthcare_system.payloads.LoginRequest;
import com.gender_healthcare_system.services.ConsultantService;
import com.gender_healthcare_system.services.ConsultationService;
import com.gender_healthcare_system.services.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultant")
@AllArgsConstructor
public class ConsultantController {

    
    private final ConsultationService consultationService;
    
    private final ConsultantService consultantService;

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

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public String logout(@RequestBody String token) {
        jwtService.isTokenBlacklisted(token);
        return "Logout successful";
    }

    //Get all consultations by consultant ID
    @GetMapping("/consultations/consultantId/{id}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<List<ConsultationsDTO>>
    getConsultations(@PathVariable int id) {
        return ResponseEntity.ok(consultationService
                .getConsultationsByConsultantId(id));
    }

    //Get consultation by ID
    @GetMapping("/consultations/{id}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ConsultantConsultationDTO getConsultationById(@PathVariable int id) {
        return consultationService.getConsultationById(id);
    }

    //register consultation
    @PostMapping("/consultations/register")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> registerConsulation
    (@RequestBody ConsultationRegisterPayload payload) {

        consultationService.registerConsultation(payload);
        return ResponseEntity.ok("Consultation registered successfully");
    }

    //Confirm consultation
    @PostMapping("/consultations/confirm")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> confirmConsultation(@RequestBody ConsultationConfirmPayload payload) {
        consultationService.confirmConsultation(payload);
        return ResponseEntity.ok("Consultation confirmed successfully");
    }

    //Cancel consultation
    //Can consultant actually cancel consultation
    @PostMapping("/consultations/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> cancelConsultation
    (@PathVariable int id) {
        consultationService.cancelConsultation(id);
        return ResponseEntity.ok("Consultation cancelled successfully");
    }

    //Reschedule consultation
    @PostMapping("/consultations/reschedule")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> reScheduleConsultation
    (@RequestBody ConsultationConfirmPayload payload) {

        consultationService.reScheduleConsultation(payload);
        return ResponseEntity.ok("Consultation rescheduled successfully");
    }

    //Reschedule consultation
    @PostMapping("/consultations/complete")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> completeConsultation
    (@RequestBody ConsultationCompletePayload payload) {

        consultationService.completeConsultation(payload);
        return ResponseEntity.ok("Consultation rescheduled successfully");
    }
}
