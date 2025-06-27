package com.gender_healthcare_system.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListConsultantDTO implements Serializable {

    private Integer consultantId;
    private String fullName;
    private String avatarUrl;
    private String phone;
    private String email;
    private String address;

}
