package com.gender_healthcare_system.dtos.user;

import com.gender_healthcare_system.entities.enu.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDTO implements Serializable {

    private int staffId;
    private String userName;
    private String password;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private AccountStatus status;

    public StaffDTO(int staffId, String fullName, String phone, String email, String address) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
}

