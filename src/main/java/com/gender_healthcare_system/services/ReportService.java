package com.gender_healthcare_system.services;

import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import com.gender_healthcare_system.entities.enu.TestingServiceBookingStatus;
import com.gender_healthcare_system.entities.enu.TestingServiceStatus;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.ConsultationRepo;
import com.gender_healthcare_system.repositories.TestingServiceBookingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ConsultationRepo consultationRepo;
    private final TestingServiceBookingRepo testingServiceBookingRepo;
    private final AccountRepo accountRepo;

    public long countConsultations(ConsultationStatus status, int periodInDays) {
        LocalDateTime from = LocalDateTime.now().minusDays(periodInDays);
        return consultationRepo.countByStatusAndPeriod(status, from);
    }

    public long countTestingBookings(TestingServiceBookingStatus status, int periodDays) {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(periodDays);
        return testingServiceBookingRepo.countByStatusAndCreatedAtAfter(status, fromDate);
    }

    public Long getTotalRevenueFromConsultations() {
        return consultationRepo.getTotalRevenueFromConsultations();
    }

    public Long getTotalRevenueFromTestingServices() {
        return testingServiceBookingRepo.getTotalRevenueFromTestingServices();
    }

    public long getTotalUserCount() {
        return accountRepo.countTotalAccounts();
    }
}

