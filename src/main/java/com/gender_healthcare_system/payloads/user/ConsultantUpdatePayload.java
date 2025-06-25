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
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantUpdatePayload implements Serializable {

    @NotBlank(message = "Full name is required")
    @Length(min = 3,max = 70, message = "Full name must be between 3 and 70 characters")
    private String fullName;

    @NotBlank(message = "Avatar URL is required")
    @Length(min =20, max = 255, message = "Avatar URL must be between 20 and 255 characters")
    private String avatarUrl;

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
