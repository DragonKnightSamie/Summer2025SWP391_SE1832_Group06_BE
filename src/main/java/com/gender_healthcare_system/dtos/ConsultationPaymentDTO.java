package com.gender_healthcare_system.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gender_healthcare_system.entities.enu.PaymentMethod;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationPaymentDTO {

    private int consultationPaymentId;

    private double amount;

    private PaymentMethod method;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime createdAt;

    private PaymentStatus status;
}
