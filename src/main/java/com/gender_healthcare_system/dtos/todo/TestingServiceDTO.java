package com.gender_healthcare_system.dtos.todo;

import com.gender_healthcare_system.entities.enu.TestingServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceDTO implements Serializable {

    private Integer serviceId;
    private String serviceName;
    private String description;
    private TestingServiceStatus status;

    private TestingServiceTypeDTO testingServiceType;
    private PriceListDTO priceList;

    public TestingServiceDTO(Integer serviceId, String serviceName, String description, TestingServiceStatus status) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.status = status;
    }
}
