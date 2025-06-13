package com.gender_healthcare_system.dtos;

import com.gender_healthcare_system.entities.enu.TestingServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class TestingServiceDTO {

    private int serviceId;
    private String serviceName;
    private String description;
    private TestingServiceStatus status;

    private TestingServiceFormDTO testingServiceForm;
    private TestingServiceTypeDTO testingServiceType;
    private PriceListDTO priceList;

    public TestingServiceDTO(int serviceId, String serviceName, String description,
                             TestingServiceStatus status,
                             TestingServiceFormDTO testingServiceForm,
                             TestingServiceTypeDTO testingServiceType,
                             PriceListDTO priceList) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.status = status;
        this.testingServiceForm = testingServiceForm;
        this.testingServiceType = testingServiceType;
        this.priceList = priceList;
    }

    public TestingServiceDTO(int serviceId, String serviceName, String description, TestingServiceStatus status) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.status = status;
    }
}
