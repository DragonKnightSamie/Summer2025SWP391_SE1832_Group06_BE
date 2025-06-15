package com.gender_healthcare_system.entities.todo;


import com.gender_healthcare_system.entities.enu.Rating;
import com.gender_healthcare_system.entities.user.Consultant;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

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

    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL)
    private ConsultationPayment consultationPayment ;

    // Many to one relationship with Consultant
    @ManyToOne
    @JoinColumn(name = "consultant_id", nullable = false)
    private Consultant consultant;

    // Many to one relationship with Customer
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", length = 15)
    private Rating rating;

    @Nationalized
    @Column(name = "comment", length = 255)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expected_start_time", nullable = false)
    private LocalDateTime expectedStartTime;

    @Column(name = "real_start_time")
    private LocalDateTime realStartTime;

    @Column(name = "expected_end_time", nullable = false)
    private LocalDateTime expectedEndTime;

    @Column(name = "real_end_time")
    private LocalDateTime realEndTime;

    //Enum
    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private ConsultationStatus status;

    public Consultation(int consultationId, LocalDateTime createdAt,
                        LocalDateTime expectedStartTime, LocalDateTime realStartTime,
                        LocalDateTime expectedEndTime, LocalDateTime realEndTime,
                        ConsultationStatus status) {
        this.consultationId = consultationId;
        this.createdAt = createdAt;
        this.expectedStartTime = expectedStartTime;
        this.realStartTime = realStartTime;
        this.expectedEndTime = expectedEndTime;
        this.realEndTime = realEndTime;
        this.status = status;
    }
}
