package com.gender_healthcare_system.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gender_healthcare_system.entities.user.Consultant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDTO {

    private int certificateId;

    @Nationalized
    private String certificateName;

    @Nationalized
    private String issuedBy;

    @Schema(type = "string", example = "05/06/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate issueDate;

    @Schema(type = "string", example = "05/06/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate expiryDate;

    @Nationalized
    private String description;
}
