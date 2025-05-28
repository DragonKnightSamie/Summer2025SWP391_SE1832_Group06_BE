package com.gender_healthcare_system.entities.user;

import com.gender_healthcare_system.entities.todo.Payment;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Entity
@Table(name = "Staff")
public class Staff {

    @Id
    @SequenceGenerator(name = "staff_sequence", sequenceName = "staff_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_sequence")
    @Column(name = "staff_id")
    private int staffId;

    @Column(name = "full_name", nullable = false, length = 70)
    @Nationalized
    private String fullName;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "address", nullable = false, length = 255)
    @Nationalized
    private String address;

    //One-to-Many relationship with Payment
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;
}
