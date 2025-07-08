package com.gender_healthcare_system.dtos.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gender_healthcare_system.entities.enu.MenstrualSeverity;
import com.gender_healthcare_system.entities.enu.MenstrualStatus;
import com.gender_healthcare_system.entities.user.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenstrualCycleDTO {
    private Integer cycleId;
    private LocalDate startDate;
    private Integer cycleLength;
    private Boolean isTrackingEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Account customer;
    private MenstrualSeverity severity;
    private MenstrualStatus status;
    private String note;

    public MenstrualCycleDTO(Integer cycleId, LocalDate startDate, Integer cycleLength, Boolean isTrackingEnabled, LocalDateTime createdAt, LocalDateTime updatedAt, Account customer) {
        this(cycleId, startDate, cycleLength, isTrackingEnabled, createdAt, updatedAt, customer, null, null, null);
    }
}
