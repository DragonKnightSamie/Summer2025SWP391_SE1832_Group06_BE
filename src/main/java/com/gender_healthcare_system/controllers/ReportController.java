package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.entities.enu.TestingServiceBookingStatus;
import com.gender_healthcare_system.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*@RestController
@RequestMapping("/api/v1/statistic-reports")
@RequiredArgsConstructor*/
public class ReportController {

    /*private final ReportService reportService;

    @GetMapping("/consultations")
    public ResponseEntity<?> countConsultations(@RequestParam int periodByDays) {

        return ResponseEntity.ok(reportService.getConsultationsStatistics(periodByDays));
    }

    @GetMapping("/testing-service-bookings")
    public ResponseEntity<?> countTestingBookings(@RequestParam int periodDays) {

        return ResponseEntity.ok(
                reportService.getTestingBookingsStatistics(periodDays));
    }*/

    /*@GetMapping("/consultations/revenue")
    public ResponseEntity<?> getConsultationRevenue(@RequestParam int periodDays) {

        return ResponseEntity.ok
                (reportService.getTotalRevenueFromConsultations(periodDays));
    }

    @GetMapping("/testing-services/revenue")
    public ResponseEntity<?> getTestingServiceRevenue(
            @RequestParam PaymentStatus status,
            @RequestParam int periodDays) {

        Long total = reportService.getTotalRevenueFromTestingServices(status, periodDays);
        return ResponseEntity.ok(total != null ? total : 0L);
    }*/

    /*@GetMapping("/users/count")
    public ResponseEntity<?> getTotalUserCount() {
        long count = reportService.getTotalUserCount();
        return ResponseEntity.ok(count);
    }*/
}

