package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.entities.enu.Rating;
import com.gender_healthcare_system.entities.enu.TestingServiceHistoryStatus;
import com.gender_healthcare_system.entities.user.Staff;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Entity
@Table(name = "TestingServiceHistory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceHistory {

    @Id
    @SequenceGenerator(name = "testing_service_history_seq", sequenceName = "testing_service_history_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testing_service_history_seq")
    @Column(name = "service_history_id")
    private int serviceHistoryId;

    // Relationship with TestingService
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private TestingService testingService;

    // Relationship with Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Relationship with Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @Nationalized
    @Column(name = "result", nullable = false, length = 255)
    private String result;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false)
    private Rating rating;

    @Column(name = "comment", length = 255)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TestingServiceHistoryStatus status;

    //One-to-One relationship with Payment
    @OneToOne(mappedBy = "testingServiceHistory")
    private Payment payment;

}
