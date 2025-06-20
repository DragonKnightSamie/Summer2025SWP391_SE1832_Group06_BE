package com.gender_healthcare_system.dtos.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationsDTO implements Serializable {

    private int consultationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime expectedStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime realStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime expectedEndTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime realEndTime;

    private ConsultationStatus status;

}
