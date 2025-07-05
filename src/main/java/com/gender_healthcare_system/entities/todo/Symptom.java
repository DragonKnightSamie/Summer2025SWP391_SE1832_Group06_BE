package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.user.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Symptom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long symptomId;

    @Column(nullable = false, length = 100)
    private String name; // đau đau, buồn nôn, chóng mặt, v.v.

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Account customer;
}
