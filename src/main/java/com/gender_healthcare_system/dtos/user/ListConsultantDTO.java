package com.gender_healthcare_system.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListConsultantDTO {

    private int consultantId;
    private String fullName;
    private String avatarUrl;
    private String phone;
    private String email;
    private String address;

}
