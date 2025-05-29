package com.gender_healthcare_system.entities.user;


import com.gender_healthcare_system.entities.enu.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    @Column(name = "account_id")
    private int accountId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    //Enum
    @Column(name = "status", nullable = false, length = 15)
    private AccountStatus status;

/*    @Column(name = "role_id", nullable = false)
    private int roleId;*/

    //ManyToOne relationship with Role
    @ManyToOne
    //@MapsId
    @JoinColumn(name = "role_id")
    private Role role;

    //, insertable = false, updatable = false, nullable = false
}
