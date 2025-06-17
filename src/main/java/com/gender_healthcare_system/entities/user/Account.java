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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    @Column(name = "account_id")
    private int accountId;

    //ManyToOne relationship with Role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    //Enum
    @Column(name = "status", nullable = false, length = 15)
    private AccountStatus status;

    // nếu xóa tài khoản thì sẽ xóa luôn các thông tin trong cac role
    /*@OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Staff staff;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Consultant consultant;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer customer;*/

    public Account(int accountId, String username, String password, Role role) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
