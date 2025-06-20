package com.gender_healthcare_system.dtos.todo;

import com.gender_healthcare_system.entities.enu.TestingServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceListDTO implements Serializable {

    private int serviceId;
    private String serviceName;
    private String serviceTypeName;
    private String description;
    private TestingServiceStatus status;
}
