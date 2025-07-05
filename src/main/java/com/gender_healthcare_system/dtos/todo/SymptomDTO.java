package com.gender_healthcare_system.dtos.todo;

import com.gender_healthcare_system.entities.user.Account;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymptomDTO {
    private Long symptomId;
    private String name;
    private String description;
    private LocalDateTime recordedAt;
    private Account customer;
}
