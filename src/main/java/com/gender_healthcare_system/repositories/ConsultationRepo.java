package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.ConsultantConsultationDTO;
import com.gender_healthcare_system.dtos.ConsultationsDTO;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.payloads.ConsultationCompletePayload;
import com.gender_healthcare_system.payloads.ConsultationConfirmPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepo extends JpaRepository<Consultation, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos" +
            ".ConsultationsDTO(c.consultationId, c.createdAt, " +
            "c.expectedStartTime, c.realStartTime, c.expectedEndTime, c.realEndTime, " +
            "c.status) " +
            "FROM Consultation c " +
            "JOIN c.customer " +
            "WHERE c.customer.customerId = :customerId")
    List<ConsultationsDTO> findByCustomerId(int customerId);

    @Query("SELECT new com.gender_healthcare_system.dtos" +
            ".ConsultationsDTO(c.consultationId, c.createdAt, " +
            "c.expectedStartTime, c.realStartTime, c.expectedEndTime, c.realEndTime, " +
            "c.status) " +
            "FROM Consultation c " +
            "JOIN c.consultant " +
            "WHERE c.consultant.consultantId = :consultantId")
    List<ConsultationsDTO> findByConsultantId(int consultantId);

    @Query("SELECT new com.gender_healthcare_system.entities.todo" +
            ".Consultation(c.consultationId, c.createdAt, " +
            "c.expectedStartTime, c.realStartTime, c.expectedEndTime, c.realEndTime, " +
            "c.status) " +
            "FROM Consultation c " +
            "JOIN c.consultant " +
            "WHERE c.consultationId = :id")
    Optional<Consultation> findConsultationById(int id);

    @Query("SELECT new com.gender_healthcare_system.dtos" +
            ".ConsultantConsultationDTO(c.consultationId, c.createdAt, " +
            "c.expectedStartTime, c.realStartTime, c.expectedEndTime, c.realEndTime, " +
            "c.status, new com.gender_healthcare_system.dtos.CustomerDTO" +
            "(cu.customerId, cu.fullName, cu.dateOfBirth, cu.gender, " +
            "cu.genderSpecificDetails, cu.phone, cu.email, cu.address)" +
            ") " +
            "FROM Consultation c " +
            "JOIN c.customer cu " +
            "WHERE c.consultationId = :id")
    Optional<ConsultantConsultationDTO> getConsultationDetailsById(int id);

    @Modifying
    @Query("UPDATE Consultation c SET " +
            "c.expectedStartTime = :#{#payload.expectedStartTime}, " +
            "c.expectedEndTime = :#{#payload.expectedEndTime}, " +
            "c.status = :status " +
            "WHERE c.consultationId = :#{#payload.consultationId}")
    void updateConsultation(@Param("payload") ConsultationConfirmPayload payload,
                            ConsultationStatus status);

    @Modifying
    @Query("UPDATE Consultation c SET " +
            "c.realStartTime = :#{#payload.realStartTime}, " +
            "c.realEndTime = :#{#payload.realEndTime}, " +
            "c.status = :status " +
            "WHERE c.consultationId = :#{#payload.consultationId}")
    void completeConsultation(@Param("payload") ConsultationCompletePayload payload,
                              ConsultationStatus status);

    @Modifying
    @Query("UPDATE Consultation c SET " +
            "c.status = com.gender_healthcare_system.entities.enu.ConsultationStatus.CANCELLED " +
            "WHERE c.consultationId = :id")
    void cancelConsultation(int id);
}
