package com.gender_healthcare_system.entities.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gender_healthcare_system.entities.user.Consultant;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    // Many to one relationship with Consultant
    @ManyToOne
    @JoinColumn(name = "consultant_id", nullable = false)
    private Consultant consultant;

    // Many to one relationship with Customer
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "created_at", nullable = false)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    //@DateTimeFormat(pattern = "DD-MM-YYYY HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "expected_start_time", nullable = false)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    //@DateTimeFormat(pattern = "DD-MM-YYYY HH:mm:ss")
    private LocalDateTime expectedStartTime;

    @Column(name = "real_start_time")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    //@DateTimeFormat(pattern = "DD-MM-YYYY HH:mm:ss")
    private LocalDateTime realStartTime;

    @Column(name = "expected_end_time", nullable = false)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    //@DateTimeFormat(pattern = "DD-MM-YYYY HH:mm:ss")
    private LocalDateTime expectedEndTime;

    @Column(name = "real_end_time")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:")
    //@DateTimeFormat(pattern = "DD-MM-YYYY HH:mm:ss")
    private LocalDateTime realEndTime;

    //Enum
    @Column(name = "status", nullable = false, length = 20)
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
