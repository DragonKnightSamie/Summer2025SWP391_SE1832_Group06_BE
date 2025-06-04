package com.gender_healthcare_system.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationCompletePayload {

    @NotNull
    private int consultationId;

    @NotNull
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "DD-MM-YYYY HH:mm:ss")
    private LocalDateTime realStartTime;

    @NotNull
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "DD-MM-YYYY HH:mm:ss")
    private LocalDateTime realEndTime;
}
