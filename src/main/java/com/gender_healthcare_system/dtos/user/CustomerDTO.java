package com.gender_healthcare_system.dtos.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gender_healthcare_system.entities.enu.AccountStatus;
import com.gender_healthcare_system.entities.enu.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements Serializable {

    private Integer customerId;

    private String userName;

    private String password;

    private String fullName;

    @Schema(type = "string", example = "05/06/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    private Gender gender;

    private String genderSpecificDetails;

    private CustomerPeriodDetailsDTO periodDetails;

    private String phone;

    private String email;

    private String address;

    private AccountStatus status;

    public CustomerDTO(Integer customerId, String userName, String password,
                       String fullName, LocalDate dateOfBirth, Gender gender,
                       String genderSpecificDetails, String phone, String email,
                       String address, AccountStatus status) {
        this.customerId = customerId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.genderSpecificDetails = genderSpecificDetails;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = status;
    }

    public CustomerDTO(Integer customerId, String fullName, LocalDate dateOfBirth,
                       Gender gender, String genderSpecificDetails, String phone,
                       String email, String address) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.genderSpecificDetails = genderSpecificDetails;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public CustomerDTO(Integer customerId, String userName, String fullName,
                       Gender gender, AccountStatus status) {
        this.customerId = customerId;
        this.userName = userName;
        this.fullName = fullName;
        this.gender = gender;
        this.status = status;
    }
}
