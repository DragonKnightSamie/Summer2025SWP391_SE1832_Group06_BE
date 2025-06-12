package com.gender_healthcare_system.dtos;

import com.gender_healthcare_system.entities.enu.TestingServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceListDTO {

    private int serviceId;
    private String serviceName;
    private String serviceTypeName;
    private String description;
    private TestingServiceStatus status;
}
