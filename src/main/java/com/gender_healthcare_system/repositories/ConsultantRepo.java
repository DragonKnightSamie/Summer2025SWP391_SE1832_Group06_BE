package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.user.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultantRepo extends JpaRepository<Consultant, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.LoginResponse(" +
            "c.consultantId, c.fullName, c.email) FROM Consultant c " +
            "WHERE c.consultantId = :id")
    LoginResponse getConsultantLoginDetails(int id);

    @Query("SELECT new com.gender_healthcare_system.entities.user.Consultant" +
            "(c.consultantId, c.fullName, c.phone, c.email, c.address) " +
            "FROM Consultant c " +
            "WHERE c.consultantId = :id")
    Optional<Consultant> getConsultantById(int id);
}
