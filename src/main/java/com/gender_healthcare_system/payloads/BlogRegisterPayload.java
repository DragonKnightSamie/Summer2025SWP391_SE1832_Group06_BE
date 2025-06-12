package com.gender_healthcare_system.payloads;

import com.gender_healthcare_system.entities.enu.BlogStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogRegisterPayload {

    @NotNull(message = "Manager ID is required")
    private int managerId;

    @Nationalized
    @NotBlank
    @Length(min = 5, max = 100, message = "Blog title by must be between 3 and 100 characters")
    private String title;

    @Nationalized
    @NotBlank
    @Length(min = 10, max = 1000,
            message = "Blog content by must be between 10 and 1000 characters")
    private String content;

}
