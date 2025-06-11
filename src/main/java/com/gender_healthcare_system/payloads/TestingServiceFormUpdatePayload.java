package com.gender_healthcare_system.payloads;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceFormUpdatePayload {

    @NotNull
    private int serviceFormId;

    @Nationalized
    @Length(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String content;


}
