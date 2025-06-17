package com.gender_healthcare_system.dtos.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceResultDTO {

    private int serviceResultId;

    private String content;

    private String description;
}
