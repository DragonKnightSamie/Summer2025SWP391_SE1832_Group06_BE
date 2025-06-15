package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.enu.PaymentMethod;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TestingServicePayment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServicePayment {

    @Id
    //@SequenceGenerator(name = "payment_seq", sequenceName = "payment_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @Column(name = "service_payment_id")
    private int servicePaymentId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "service_payment_id")
    private TestingServiceBooking testingServiceBooking;

    /*@Column(name = "service_history_id", nullable = false)
    private int serviceHistoryId;*/

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
    /*@ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;
*/

}
