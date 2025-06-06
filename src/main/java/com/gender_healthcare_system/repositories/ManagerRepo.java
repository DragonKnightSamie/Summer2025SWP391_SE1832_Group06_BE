package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.entities.user.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ManagerRepo extends JpaRepository<Manager, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.LoginResponse(" +
            "m.managerId, m.fullName, m.email) FROM Manager m " +
            "WHERE m.managerId = :id")
    LoginResponse getManagerLoginDetails(int id);

    @Query("SELECT new com.gender_healthcare_system.entities.user.Customer" +
            "(c.customerId, c.fullName, c.dateOfBirth, c.gender," +
            "c.genderSpecificDetails, c.phone, c.email, c.address) " +
            "FROM Customer c " +
            "WHERE c.customerId = :id")
    Optional<Customer> getCustomerById(int id);
}
