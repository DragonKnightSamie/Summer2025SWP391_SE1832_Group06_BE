package com.gender_healthcare_system.payloads.todo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MenstrualCycleUpdatePayload {
    private LocalDate startDate;
    private Integer cycleLength;
    private Boolean isTrackingEnabled;
}
