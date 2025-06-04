package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StaffRepo extends JpaRepository<Staff, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.LoginResponse(" +
            "s.staffId, s.fullName, s.email) FROM Staff s " +
            "WHERE s.staffId = :id")
    LoginResponse getStaffLoginDetails(int id);
}
