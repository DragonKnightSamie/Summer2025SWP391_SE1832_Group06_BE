package com.gender_healthcare_system.dtos.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gender_healthcare_system.dtos.user.CustomerDTO;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantConsultationDTO implements Serializable {

    private Integer consultationId;

    @Schema(type = "string", example = "05/06/2025 07:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @Schema(type = "string", example = "05/06/2025 07:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime expectedStartTime;

    @Schema(type = "string", example = "05/06/2025 07:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime realStartTime;

    @Schema(type = "string", example = "05/06/2025 07:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime expectedEndTime;

    @Schema(type = "string", example = "05/06/2025 07:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime realEndTime;

    @Schema(type = "string", example = "Tư vấn sức khỏe sinh sản tổng quát")
    private String description;

    private ConsultationStatus status;

    private CustomerDTO customerDetails;

}
