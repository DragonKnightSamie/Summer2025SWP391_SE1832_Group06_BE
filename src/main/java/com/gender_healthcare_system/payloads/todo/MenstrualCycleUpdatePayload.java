package com.gender_healthcare_system.payloads.todo;

import java.time.LocalDate;

import com.gender_healthcare_system.entities.enu.MenstrualSeverity;
import com.gender_healthcare_system.entities.enu.MenstrualStatus;

import lombok.Data;

@Data
public class MenstrualCycleUpdatePayload {
    private LocalDate startDate;
    private Integer cycleLength;
    private Boolean isTrackingEnabled;
    private MenstrualSeverity severity;
    private MenstrualStatus status;
    private String note;
}
