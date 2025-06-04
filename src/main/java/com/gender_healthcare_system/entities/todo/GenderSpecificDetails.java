package com.gender_healthcare_system.entities.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderSpecificDetails {

    private Boolean hasMenstrualCycle; // true nếu có, false nếu không

    private Integer cycleLengthDays;

    @DateTimeFormat(pattern = "DD-MM-YYYY")
    private LocalDate lastCycleStart;

    private String notes;
}

