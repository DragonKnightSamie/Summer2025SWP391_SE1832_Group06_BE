package com.gender_healthcare_system.dtos.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPeriodDetailsDTO implements Serializable {

    private Boolean hasMenstrualCycle; // true nếu có, false nếu không

    private Integer cycleLengthDays;

    @Schema(type = "string", example = "05/06/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate lastCycleStart;

    private String notes;
}
