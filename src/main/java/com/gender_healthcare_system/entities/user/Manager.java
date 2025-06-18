package com.gender_healthcare_system.entities.user;

import com.gender_healthcare_system.entities.todo.Blog;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "Manager")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {

    @Id
    //@SequenceGenerator(name = "manager_sequence", sequenceName = "manager_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_sequence")
    @Column(name = "manager_id")
    private int managerId;

    @Column(name = "full_name", nullable = false, length = 70)
    @Nationalized  // Allows for Unicode characters
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "address", nullable = false, length = 255)
    @Nationalized  // Allows for Unicode characters
    private String address;

    // Relationship with Blog
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    @JsonManagedReference   // <-- Thêm annotation này, tranh vong lap vo han không parse được JSON
    private List<Blog> blogs;

    // One-to-One relationship with Account
    @OneToOne
    @MapsId
    @JoinColumn(name = "manager_id", nullable = false)
    private Account account;

    public Manager(int managerId, String fullName, String email,
                   String phone, String address) {
        this.managerId = managerId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
