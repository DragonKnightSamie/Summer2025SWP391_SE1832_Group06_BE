package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.user.Consultant;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Consultation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {

    @Id
    @SequenceGenerator(name = "consultation_sequence", sequenceName = "consultation_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consultation_sequence")
    @Column(name = "consultation_id")
    private int consultationId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expected_start_time", nullable = false)
    private LocalDateTime expectedStartTime;

    @Column(name = "real_start_time", nullable = true)
    private LocalDateTime realStartTime;

    @Column(name = "expected_end_time", nullable = false)
    private LocalDateTime expectedEndTime;

    @Column(name = "real_end_time", nullable = true)
    private LocalDateTime realEndTime;

    //Enum
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ConsultationStatus status;


    // Many to one relationship with Consultant
    @ManyToOne
    @JoinColumn(name = "consultant_id", nullable = false)
    private Consultant consultant;

    // Many to one relationship with Customer
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
