package com.gender_healthcare_system.payloads.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceTypeUpdatePayload implements Serializable {

    @Nationalized
    @NotBlank
    @Length(min = 5, max = 100, message = "Service type name must be between 5 and 100 characters")
    private String serviceTypeName;

    @Nationalized
    @NotBlank
    @Length(min = 5, max = 100, message = "Service type title must be between 5 and 100 characters")
    private String title;

    @Nationalized
    @Size(min = 5,max = 255, message = "Service type content must be empty or between 5 and 255 characters")
    private String content;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Schema(type = "string", example = "05/06/2025 07:00")
    private LocalDateTime createdAt;
}
