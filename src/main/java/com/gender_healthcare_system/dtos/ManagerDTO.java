package com.gender_healthcare_system.dtos;

import com.gender_healthcare_system.entities.enu.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDTO {

    private int managerId;
    private String userName;
    private String password;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private AccountStatus status;
}
