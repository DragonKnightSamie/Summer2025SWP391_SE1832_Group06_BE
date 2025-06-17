package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.user.Consultant;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Certificate")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "certificate_sequence", sequenceName = "certificate_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificate_sequence")
    @Column(name = "certificate_id")
    private int certificateId;

    @ManyToOne
    @JoinColumn(name = "consultant_id", nullable = false)
    private Consultant consultant;

    @Nationalized
    @Column(name = "certificate_name", nullable = false, length = 100)
    private String certificateName;

    @Nationalized
    @Column(name = "issued_by", nullable = false, length = 100)
    private String issuedBy;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Nationalized
    @Column(name = "description", length = 255)
    private String description;

}
