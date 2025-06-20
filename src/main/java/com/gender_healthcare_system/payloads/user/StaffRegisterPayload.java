package com.gender_healthcare_system.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRegisterPayload implements Serializable {

    @NotBlank(message = "Username is required")
    @Length(min = 5, max = 50, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Length(min = 5, max = 50)
    private String password;

    @NotBlank(message = "Full name is required")
    @Length(max = 70)
    private String fullName;

    @NotBlank(message = "Phone is required")
    @Length(max = 15)
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be a Gmail address")
    private String email;

    @NotBlank(message = "Address is required")
    @Length(max = 100)
    private String address;


}

