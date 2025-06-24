package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import com.gender_healthcare_system.entities.enu.TestingServiceBookingStatus;
import com.gender_healthcare_system.entities.enu.TestingServiceStatus;
import com.gender_healthcare_system.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/consultations/count")
    public ResponseEntity<?> countConsultations(
            @RequestParam ConsultationStatus status,
            @RequestParam int periodDays) {
        long count = reportService.countConsultations(status, periodDays);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/testing-bookings/count")
    public ResponseEntity<?> countTestingBookings(
            @RequestParam TestingServiceBookingStatus status,
            @RequestParam int periodDays) {
        long count = reportService.countTestingBookings(status, periodDays);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/revenue/consultations")
    public ResponseEntity<?> getConsultationRevenue() {
        Long total = reportService.getTotalRevenueFromConsultations();
        return ResponseEntity.ok(total != null ? total : 0L);
    }

    @GetMapping("/revenue/testing-services")
    public ResponseEntity<?> getTestingServiceRevenue() {
        Long total = reportService.getTotalRevenueFromTestingServices();
        return ResponseEntity.ok(total != null ? total : 0L);
    }

    @GetMapping("/users/count")
    public ResponseEntity<?> getTotalUserCount() {
        long count = reportService.getTotalUserCount();
        return ResponseEntity.ok(count);
    }
}

