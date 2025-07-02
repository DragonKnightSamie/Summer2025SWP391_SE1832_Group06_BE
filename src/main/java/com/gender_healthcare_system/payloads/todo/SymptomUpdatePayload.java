package com.gender_healthcare_system.payloads.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymptomUpdatePayload {
    @NotBlank
    private String name;

    private String description;
}
