package com.gender_healthcare_system.payloads.todo;

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
    @NotBlank
    @Length(min = 5, max = 70, message = "Customer full name " +
            "must be between 5 and 70 characters")
    private String customerFullName;

    @NotNull
    private long amount;

    @Nationalized
    @NotBlank
    @Length(min = 5, max = 100, message = "Description " +
            "must be between 5 and 100 characters")
    private String description;
}
