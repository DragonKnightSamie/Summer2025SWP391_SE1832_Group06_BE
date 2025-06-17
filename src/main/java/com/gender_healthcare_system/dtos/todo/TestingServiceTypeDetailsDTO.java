package com.gender_healthcare_system.dtos.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceTypeDetailsDTO {

    private TestingServiceTypeDTO serviceTypeDetails;

    private List<TestingServiceResultDTO> serviceResultList;

    public TestingServiceTypeDetailsDTO(TestingServiceTypeDTO serviceTypeDetails) {
        this.serviceTypeDetails = serviceTypeDetails;
    }
}
