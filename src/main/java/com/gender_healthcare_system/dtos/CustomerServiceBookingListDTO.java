package com.gender_healthcare_system.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gender_healthcare_system.entities.enu.TestingServiceBookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerServiceBookingListDTO {

    private int serviceBookingId;

    private String serviceName;

    private String staffName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime createdAt;

    private TestingServiceBookingStatus status;
}
