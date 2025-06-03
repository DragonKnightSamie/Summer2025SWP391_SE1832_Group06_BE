package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.payloads.ConsultationPayload;
import com.gender_healthcare_system.payloads.LoginRequest;
import com.gender_healthcare_system.services.ConsultationService;
import com.gender_healthcare_system.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ConsultantController {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
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
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CONSULTANT"));

        if (!hasRole) {
            throw new UsernameNotFoundException("Access denied for non-consultant user");
        }

        return jwtService.generateToken(loginRequest.getUsername());
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public String logout(@RequestBody String token) {
        jwtService.isTokenBlacklisted(token);
        return "Logout successful";
    }

    //Get all consultations by consultant ID
    @GetMapping("/consultations/{consultantId}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<List<Consultation>> getConsultations(@PathVariable int consultantId) {
        return ResponseEntity.ok(consultationService.getConsultationsByConsultantId(consultantId));
    }

    //Confirm consultation
    @PostMapping("/consultations/confirm")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> confirmConsultation(@RequestBody ConsultationPayload payload) {
        consultationService.confirmConsultation(payload);
        return ResponseEntity.ok("Consultation confirmed successfully");
    }

    //Cancel consultation
    @PostMapping("/consultations/cancel")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> cancelConsultation(@RequestBody ConsultationPayload payload) {
        consultationService.cancelConsultation(payload);
        return ResponseEntity.ok("Consultation cancelled successfully");
    }

    //Update consultation
    @PostMapping("/consultations/update")
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<String> updateConsultation(@RequestBody ConsultationPayload payload) {
        consultationService.updateConsultation(payload);
        return ResponseEntity.ok("Consultation updated successfully");
    }
}
