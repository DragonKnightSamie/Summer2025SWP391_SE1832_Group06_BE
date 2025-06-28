package com.gender_healthcare_system.dtos.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse implements Serializable {

    private Integer id;
    private String username;
    private String fullname;
    private String avatarUrl;
    private String email;
    private String token;

    public LoginResponse(Integer id, String fullname, String email) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
    }

    public LoginResponse(String fullname, String avatarUrl, String email) {
        this.fullname = fullname;
        this.avatarUrl = avatarUrl;
        this.email = email;
    }
}
