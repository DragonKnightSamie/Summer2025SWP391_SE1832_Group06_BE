package com.gender_healthcare_system.payloads.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceResultCompletePayload {

    @Nationalized
    @NotBlank
    @Length(min = 5, max = 50,
            message = "Testing result title must be between 5 and 50 characters")
    private String title;

    @Nationalized
    @NotBlank
    @Length(min = 5, max = 100,
            message = "Testing result description must be between 5 and 100 characters")
    private String description;

    @Nationalized
    @NotBlank
    @Length(min = 5, max = 50,
            message = "Testing result must be between 5 and 50 characters")
    private String result;
}
