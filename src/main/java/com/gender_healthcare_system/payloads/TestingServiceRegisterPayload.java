package com.gender_healthcare_system.payloads;

import com.gender_healthcare_system.dtos.TestingServiceFormDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceRegisterPayload {

    @Nationalized
    @NotBlank(message = "Service name is required")
    @Length(min = 5, max = 100, message = "Certificate name must be between 5 and 100 characters")
    private String serviceName;

    @Nationalized
    @NotBlank(message = "Description is required")
    @Length(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;


    private int serviceTypeId; // foreign key tới TestingServiceType
    private int priceId; // foreign key tới Price
    private TestingServiceFormDTO form; // dữ liệu điền trong form
}
