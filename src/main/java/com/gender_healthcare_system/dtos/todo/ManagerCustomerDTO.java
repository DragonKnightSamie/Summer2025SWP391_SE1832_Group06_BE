package com.gender_healthcare_system.dtos.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gender_healthcare_system.entities.enu.AccountStatus;
import com.gender_healthcare_system.entities.enu.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerCustomerDTO implements Serializable {

    private Integer customerId;

    private String userName;

    private String password;

    private String fullName;

    @Schema(type = "string", example = "05/06/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    private Gender gender;

    private String genderSpecificDetails;

    private String phone;

    private String email;

    private String address;

    private AccountStatus status;
}
