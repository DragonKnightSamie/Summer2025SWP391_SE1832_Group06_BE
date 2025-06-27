package com.gender_healthcare_system.dtos.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTestingServiceListDTO implements Serializable {

    private Integer serviceId;
    private String serviceName;
    private String serviceTypeName;
    private String description;
}
