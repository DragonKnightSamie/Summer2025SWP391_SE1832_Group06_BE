package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.user.ConsultantDetailsDTO;
import com.gender_healthcare_system.dtos.user.ConsultantsDTO;
import com.gender_healthcare_system.dtos.user.ListConsultantDTO;
import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.entities.user.Consultant;
import com.gender_healthcare_system.payloads.user.ConsultantUpdatePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultantRepo extends JpaRepository<Consultant, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.login.LoginResponse(" +
            "c.consultantId, c.fullName, c.email) FROM Consultant c " +
            "WHERE c.consultantId = :id")
    LoginResponse getConsultantLoginDetails(int id);

    // Get all consultants for customer view
    @Query("SELECT new com.gender_healthcare_system.dtos.user.ListConsultantDTO" +
            "(c.consultantId,c.fullName, c.phone, " +
            "c.email, c.address) " +
            "FROM Consultant c")
    List<ListConsultantDTO> getAllConsultantsForCustomer();


    @Query("SELECT new com.gender_healthcare_system.dtos.user.ConsultantsDTO" +
            "(c.consultantId, a.username, a.password, c.fullName, c.phone, " +
            "c.email, c.address, a.status) " +
            "FROM Consultant c " +
            "JOIN c.account a")
    Page<ConsultantsDTO> getAllConsultants(Pageable pageable);

    @Query("SELECT new com.gender_healthcare_system.dtos.user.ConsultantDetailsDTO" +
            "(c.consultantId, a.username, a.password, c.fullName, c.phone, " +
            "c.email, c.address, a.status) " +
            "FROM Consultant c " +
            "JOIN c.account a " +
            "WHERE c.consultantId = :id")
    Optional<ConsultantDetailsDTO> getConsultantDetails(int id);

    @Query("SELECT new com.gender_healthcare_system.entities.user.Consultant" +
            "(c.consultantId, c.fullName, c.phone, c.email, c.address) " +
            "FROM Consultant c " +
            "WHERE c.consultantId = :id")
    Optional<Consultant> getConsultantById(int id);

    @Modifying
    @Query("UPDATE Consultant c " +
            "SET c.fullName = :#{#payload.fullName}, " +
            "c.phone = :#{#payload.phone}, " +
            "c.email = :#{#payload.email}, " +
            "c.address = :#{#payload.address} " +
            "WHERE c.consultantId = :id")
    void updateConsultant(int id, @Param("payload") ConsultantUpdatePayload payload);

    // Delete consultant by ID
    @Modifying
    //@Transactional
    @Query("DELETE FROM Consultant c WHERE c.consultantId = :id")
    void deleteConsultantById(@Param("id") int id);
}
