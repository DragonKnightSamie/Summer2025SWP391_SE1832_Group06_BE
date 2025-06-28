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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serviceTypeName;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TestingServiceStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TestingServiceTypeDTO testingServiceType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PriceListDTO priceList;

    public TestingServiceDTO(Integer serviceId, String serviceName,
                             String serviceTypeName, String description) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceTypeName = serviceTypeName;
        this.description = description;
    }

    public TestingServiceDTO(Integer serviceId, String serviceName, String serviceTypeName,
                             String description, TestingServiceStatus status) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceTypeName = serviceTypeName;
        this.description = description;
        this.status = status;
    }

    public TestingServiceDTO(Integer serviceId, String serviceName, String description,
                             TestingServiceStatus status,
                             TestingServiceTypeDTO testingServiceType,
                             PriceListDTO priceList) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.status = status;
        this.testingServiceType = testingServiceType;
        this.priceList = priceList;
    }
}
