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
    @Length(min = 3, max = 50, message = "Password must be between 3 and 50 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Length(min = 3,max = 70, message = "Full name must be between 3 and 70 characters")
    private String fullName;

    @NotBlank(message = "Phone is required")
    @Length(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be a Gmail address")
    private String email;

    @NotBlank(message = "Address is required")
    @Length(min = 3, max = 100, message = "Address must be between 3 and 100 characters")
    private String address;


}

