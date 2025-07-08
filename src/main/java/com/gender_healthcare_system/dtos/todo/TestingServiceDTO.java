package com.gender_healthcare_system.dtos.todo;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    private Long priceAmount;

    private String priceDescription;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TestingServiceStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TestingServiceTypeDTO testingServiceType;

    public TestingServiceDTO(Integer serviceId, String serviceName, String description, TestingServiceStatus status) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.status = status;
    }

    public TestingServiceDTO(Integer serviceId, String serviceName, String description,
                             TestingServiceStatus status,
                             Long priceAmount, String priceDescription,
                             TestingServiceTypeDTO testingServiceType) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.status = status;
        this.testingServiceType = testingServiceType;
        this.priceAmount = priceAmount;
        this.priceDescription = priceDescription;
    }

    public TestingServiceDTO(Integer serviceId, String serviceName, String description, Long priceAmount, String priceDescription) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.priceAmount = priceAmount;
        this.priceDescription = priceDescription;
    }
}
