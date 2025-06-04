package com.gender_healthcare_system.dtos;

import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantConsultationDTO {

    private int consultationId;
    private LocalDateTime createdAt;
    private LocalDateTime expectedStartTime;
    private LocalDateTime realStartTime;
    private LocalDateTime expectedEndTime;
    private LocalDateTime realEndTime;
    private ConsultationStatus status;
    private CustomerDTO customerDetails;
}
