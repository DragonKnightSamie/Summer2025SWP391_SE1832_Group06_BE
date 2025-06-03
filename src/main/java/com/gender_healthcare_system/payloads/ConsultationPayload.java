package com.gender_healthcare_system.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationPayload {

    private int consultationId;

    @NotNull
    private LocalDateTime expectedStartTime;

    @NotNull
    private LocalDateTime expectedEndTime;

    private String consultationType;

    private int consultantId;

    private int customerId;
}

