package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.enu.PaymentMethod;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "ConsultationPayment")
@Data
@ToString(exclude = "consultation")
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationPayment {

    @Id
    /*@SequenceGenerator(name = "consultation_payment_seq",
            sequenceName = "consultation_payment_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consultation_payment_seq")*/
    @Column(name = "consultation_payment_id")
    private int consultationPaymentId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "consultation_payment_id", nullable = false)
    private Consultation consultation;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "method", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    //Many-to-One relationship with Staff


    //Relationship with TestingService
    //1:1
    //@OneToOne
    //@JoinColumn(name = "service_id", insertable = false, updatable = false)
    //private TestingService testingService;

}
