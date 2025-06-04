package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.user.Staff;
import com.gender_healthcare_system.entities.enu.PaymentMethod;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    //@SequenceGenerator(name = "payment_seq", sequenceName = "payment_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @Column(name = "payment_id")
    private int paymentId;

    /*@Column(name = "service_history_id", nullable = false)
    private int serviceHistoryId;*/

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    //Many-to-One relationship with Staff
    /*@ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;
*/
    @OneToOne
    @MapsId
    @JoinColumn(name = "payment_id")
    private TestingServiceHistory testingServiceHistory;

}
