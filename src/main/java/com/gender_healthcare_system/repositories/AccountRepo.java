package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.entities.enu.AccountStatus;
import com.gender_healthcare_system.entities.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface AccountRepo extends JpaRepository<Account, Integer> {

    @Query("SELECT new com.gender_healthcare_system.entities.user.Account(" +
            "a.accountId, a.username, a.password, r) FROM Account AS a " +
            "JOIN a.role AS r " +
            "WHERE a.username = :username " +
            "AND a.status = " +
            "com.gender_healthcare_system.entities.enu.AccountStatus.ACTIVE")
    Optional<Account> findActiveAccountByUsername(String username);


    //update password of an account
    @Modifying
    //@Transactional
    @Query("UPDATE Account a SET a.password = :password " +
            "WHERE a.accountId = :accountId")
    void updateAccountPassword(@Param("accountId") int accountId,
                                  @Param("password") String password);

    //update password of an account
    @Modifying
    //@Transactional
    @Query("UPDATE Account a SET a.status = :status " +
            "WHERE a.accountId = :accountId")
    void updateAccountStatus(@Param("accountId") int accountId,
                               @Param("status") AccountStatus status);

    //update password of an account
    @Modifying
    //@Transactional
    @Query("DELETE FROM Account a " +
            "WHERE a.accountId = :accountId")
    void deleteAccountById(@Param("accountId") int accountId);


    boolean existsAccountByAccountIdAndStatus(int accountId, AccountStatus status);
}
