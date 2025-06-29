package com.gender_healthcare_system.entities.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderSpecificDetails implements Serializable {

    @NotNull(message = "Has MenstrualCycle is required")
    private Boolean hasMenstrualCycle; // true nếu có, false nếu không

    @Min(value = 21, message =
            "Cycle length days should be equal to or greater than 21 days if provided")
    @Max(value = 45, message =
            "Cycle length days should be equal to or less than 45 days if provided")
    private Integer cycleLengthDays;

    @Schema(type = "string", example = "05/06/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate lastCycleStart;

    @Length(min = 5, max = 255, message = "Notes must be 5 to 255 characters if provided")
    private String notes;

}

