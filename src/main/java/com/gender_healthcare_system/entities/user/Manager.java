package com.gender_healthcare_system.entities.user;

import com.gender_healthcare_system.entities.todo.Blog;
import com.gender_healthcare_system.entities.todo.TestingServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Entity
@Table(name = "Manager")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {

    @Id
    @SequenceGenerator(name = "manager_sequence", sequenceName = "manager_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_sequence")
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

    //Relationship with Blog
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Blog> blogs;

    //One-to-One relationship with Account
    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "account_id", nullable = false, unique = true)
    private Account account;
}
