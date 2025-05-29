package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.entities.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AccountRepo extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountsByUsername(String username);
}
