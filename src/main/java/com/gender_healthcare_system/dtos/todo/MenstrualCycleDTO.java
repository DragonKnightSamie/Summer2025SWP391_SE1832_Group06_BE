package com.gender_healthcare_system.dtos.todo;

import com.gender_healthcare_system.entities.user.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenstrualCycleDTO {
    private Long cycleId;
    private LocalDate startDate;
    private Integer cycleLength;
    private Boolean isTrackingEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Account customer;
}
