package com.gender_healthcare_system.entities.todo;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class GenderSpecificDetails {

    private Boolean hasMenstrualCycle; // true nếu có, false nếu không

    private Integer cycleLengthDays;

    private LocalDateTime lastCycleStart;

    private String notes;
}

