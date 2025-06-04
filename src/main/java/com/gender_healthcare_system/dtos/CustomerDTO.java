package com.gender_healthcare_system.dtos;

import com.gender_healthcare_system.entities.enu.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private int customerId;
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String genderSpecificDetails;
    private String phone;
    private String email;
    private String address;

}
