package com.gender_healthcare_system.dtos.todo;

import com.gender_healthcare_system.entities.enu.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantDetailsDTO {

    private int consultantId;
    private String userName;
    private String password;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private AccountStatus status;
    private List<CertificateDTO> certificateList;

    public ConsultantDetailsDTO(int consultantId, String userName, String password,
                                String fullName, String phone, String email,
                                String address, AccountStatus status) {
        this.consultantId = consultantId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = status;
    }
}
