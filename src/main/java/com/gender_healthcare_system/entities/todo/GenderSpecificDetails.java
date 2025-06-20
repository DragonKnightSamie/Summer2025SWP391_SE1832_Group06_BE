package com.gender_healthcare_system.entities.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderSpecificDetails implements Serializable {

    private Boolean hasMenstrualCycle; // true nếu có, false nếu không

    private Integer cycleLengthDays;

    @Schema(type = "string", example = "05/06/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate lastCycleStart;

    private String notes;
}

