package com.gender_healthcare_system.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceTypeDTO {

    private int serviceTypeId;
    private String serviceTypeName;
    private String title;
    private String content;
    private LocalDateTime createdAt;


}
