package com.gender_healthcare_system.payloads;

public class LoginRespose {

    private String token;
    private String username;
    private String status;
    private String role;

    public LoginRespose() {
    }

    public LoginRespose(String token, String username, String status, String role) {
        this.token = token;
        this.username = username;
        this.status = status;
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
