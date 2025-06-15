package com.gender_healthcare_system.payloads;

import com.gender_healthcare_system.entities.enu.TestingServiceStatus;
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
public class TestingServiceUpdatePayload {

/*    @NotNull
    private int serviceId; // ID của dịch vụ xét nghiệm, dùng để cập nhật*/

    @Nationalized
    @NotBlank(message = "Service name is required")
    @Length(min = 5, max = 100, message = "Certificate name must be between 5 and 100 characters")
    private String serviceName;

    @Nationalized
    @NotBlank(message = "Description is required")
    @Length(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    @NotNull(message = "Status is required")
    private TestingServiceStatus status;


}
