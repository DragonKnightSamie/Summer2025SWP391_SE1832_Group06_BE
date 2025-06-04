package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.user.Consultant;
import com.gender_healthcare_system.entities.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.LoginResponse(" +
            "c.customerId, c.fullName, c.email) FROM Customer c " +
            "WHERE c.customerId = :id")
    LoginResponse getCustomerLoginDetails(int id);

    @Query("SELECT new com.gender_healthcare_system.entities.user.Customer" +
            "(c.customerId, c.fullName, c.dateOfBirth, c.gender," +
            "c.genderSpecificDetails, c.phone, c.email, c.address) " +
            "FROM Customer c " +
            "WHERE c.customerId = :id")
    Optional<Customer> getCustomerById(int id);
}
