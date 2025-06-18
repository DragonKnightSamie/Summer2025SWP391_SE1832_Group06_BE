package com.gender_healthcare_system.dtos.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTestingServiceListDTO {

    private int serviceId;
    private String serviceName;
    private String serviceTypeName;
    private String description;
}
