package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.entities.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {
}
