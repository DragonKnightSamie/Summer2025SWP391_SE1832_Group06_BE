package com.gender_healthcare_system.payloads.todo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MomoPaymentPayload implements Serializable {

    @Nationalized
    @NotBlank(message = "Customer full name is required")
    @Length(min = 5, max = 70, message = "Customer full name " +
            "must be between 5 and 70 characters")
    private String customerFullName;

    @NotNull(message = "Amount is required")
    @Min(value = 10000, message = "Value must be at least 10,000 VND")
    @Max(value = 20000000, message = "Value must not exceed 20,000,000 VND")
    private Long amount;

    @Nationalized
    @NotBlank(message = "Description is required")
    @Length(min = 5, max = 100, message = "Description " +
            "must be between 5 and 100 characters")
    private String description;

    @NotBlank(message = "Redirect URL is required")
    @Length(min = 5, max = 255, message = "Redirect URL " +
            "must be between 5 and 255 characters")
    private String redirectUrl;
}
