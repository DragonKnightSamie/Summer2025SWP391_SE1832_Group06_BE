package com.gender_healthcare_system.dtos.todo;

import java.time.LocalDateTime;

import com.gender_healthcare_system.entities.enu.SymptomSeverity;
import com.gender_healthcare_system.entities.enu.SymptomStatus;
import com.gender_healthcare_system.entities.user.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymptomDTO {
    private Integer symptomId;
    private String name;
    private String description;
    private LocalDateTime recordedAt;
    private Account customer;
    private SymptomSeverity severity;
    private SymptomStatus status;
    private String note;

    public SymptomDTO(Integer symptomId, String name, String description, LocalDateTime recordedAt, Account customer) {
        this(symptomId, name, description, recordedAt, customer, null, null, null);
    }
}
