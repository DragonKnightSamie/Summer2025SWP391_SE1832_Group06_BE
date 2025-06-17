package com.gender_healthcare_system.entities.user;

import com.gender_healthcare_system.entities.enu.Gender;
import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.entities.todo.TestingServiceBooking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "customer_sequence", sequenceName = "customer_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "full_name", nullable = false, length = 70)
    @Nationalized  // Allows for Unicode characters
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "gender_specific_details", nullable = false, length = 255)
    private String genderSpecificDetails;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "address", nullable = false, length = 255)
    @Nationalized  // Allows for Unicode characters
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consultation> consultations;

    //Relationship with TestingServiceBooking
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestingServiceBooking> testingServiceHistories;

    //One-to-One relationship with Account
    @OneToOne
    @MapsId // ✅ Map customerId = account.accountId
    @JoinColumn(name = "customer_id", nullable = false)
    private Account account;

    public Customer(int customerId, String fullName, LocalDate dateOfBirth, Gender gender,
                    String genderSpecificDetails, String email, String phone, String address) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.genderSpecificDetails = genderSpecificDetails;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

}
