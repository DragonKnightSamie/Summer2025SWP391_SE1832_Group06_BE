package com.gender_healthcare_system.dtos.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantLoginResponse {

    private Integer id;
    private String username;
    private String fullname;
    private String avatarUrl;
    private String email;
    private String token;

    public ConsultantLoginResponse(Integer id, String fullname, String avatarUrl, String email) {
        this.id = id;
        this.fullname = fullname;
        this.avatarUrl = avatarUrl;
        this.email = email;
    }
}
