package com.gender_healthcare_system.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceFormDTO {

    private int serviceFormId;
    private String content;
}
