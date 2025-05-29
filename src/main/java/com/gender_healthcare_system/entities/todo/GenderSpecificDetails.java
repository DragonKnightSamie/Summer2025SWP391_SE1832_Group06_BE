package com.gender_healthcare_system.entities.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class GenderSpecificDetails {

    @Column(name = "menstrual_cycle")
    private Boolean hasMenstrualCycle; // true nếu có, false nếu không

    @Column(name = "cycle_length_days")
    private Integer cycleLengthDays;

    @Column(name = "last_cycle_start")
    private LocalDateTime lastCycleStart;

    @Column(name = "notes", length = 255)
    private String notes;
}

