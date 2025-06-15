package com.gender_healthcare_system.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantScheduleDTO {

    private int consultantId;

    private List<LocalDateTime> schedule;
}
