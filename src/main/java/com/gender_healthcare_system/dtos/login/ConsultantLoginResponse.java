package com.gender_healthcare_system.dtos.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantLoginResponse {

    private int id;
    private String username;
    private String fullname;
    private String avatarUrl;
    private String email;
    private String token;

    public ConsultantLoginResponse(int id, String fullname, String avatarUrl, String email) {
        this.id = id;
        this.fullname = fullname;
        this.avatarUrl = avatarUrl;
        this.email = email;
    }
}
