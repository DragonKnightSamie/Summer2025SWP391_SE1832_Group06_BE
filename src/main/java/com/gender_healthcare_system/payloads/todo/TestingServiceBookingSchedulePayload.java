package com.gender_healthcare_system.payloads.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceBookingSchedulePayload implements Serializable {

    @NotNull(message = "Service ID is required")
    private Integer serviceId;

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Check Date is required")
    @Schema(type = "String", example = "05/06/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate checkDate;
}
