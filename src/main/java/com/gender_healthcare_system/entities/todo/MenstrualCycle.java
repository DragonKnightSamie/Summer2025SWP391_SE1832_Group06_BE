package com.gender_healthcare_system.entities.todo;

import com.gender_healthcare_system.entities.user.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MenstrualCycle")
public class MenstrualCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cycleId;

    private LocalDate startDate;

    private Integer cycleLength = 28;

    private Boolean isTrackingEnabled = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    //mqh nhiều 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Account customer;
}
