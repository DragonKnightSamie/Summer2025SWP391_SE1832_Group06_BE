package com.gender_healthcare_system.entities.user;

import com.gender_healthcare_system.entities.todo.Certificate;
import com.gender_healthcare_system.entities.todo.Consultation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Consultant")
public class Consultant {

    @Id
    //@SequenceGenerator(name = "consultant_sequence", sequenceName = "consultant_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consultant_sequence")
    @Column(name = "consultant_id")
    private int consultantId;

    @Nationalized
    @Column(name = "full_name", nullable = false, length = 70)
    private String fullName;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Nationalized
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    // Assuming that a Consultant can have multiple certificates
    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Certificate> certificates;

    // Assuming that a Consultant can have multiple consultations
    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consultation> consultations;

    //One-to-One relationship with Account
    @OneToOne
    @MapsId
    @JoinColumn(name = "consultant_id", nullable = false)
    private Account account;
}
