package com.gender_healthcare_system.dtos.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import com.gender_healthcare_system.entities.user.Consultant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationsDTO implements Serializable {

    private Integer consultationId;

    @Schema(type = "string", example = "Phan Hoang S")
    private String consultantName;

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

    @Schema(type = "string", example = "Tư vấn sức khỏe sinh sản tổng quát")
    private String description;

    @NotNull
    private ConsultationStatus status;

}
