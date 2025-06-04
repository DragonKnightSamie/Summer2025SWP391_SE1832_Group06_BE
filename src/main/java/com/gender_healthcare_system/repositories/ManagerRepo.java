package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.user.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ManagerRepo extends JpaRepository<Manager, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.LoginResponse(" +
            "m.managerId, m.fullName, m.email) FROM Manager m " +
            "WHERE m.managerId = :id")
    LoginResponse getManagerLoginDetails(int id);
}
