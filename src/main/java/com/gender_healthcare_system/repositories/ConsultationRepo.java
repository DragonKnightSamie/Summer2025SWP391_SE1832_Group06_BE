package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.ConsultantConsultationDTO;
import com.gender_healthcare_system.dtos.ConsultantScheduleDTO;
import com.gender_healthcare_system.dtos.ConsultationsDTO;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.payloads.ConsultationCompletePayload;
import com.gender_healthcare_system.payloads.ConsultationConfirmPayload;
import com.gender_healthcare_system.payloads.ConsultationEvaluatePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    Page<ConsultationsDTO> findByCustomerId(int customerId, Pageable pageable);

    @Query("SELECT new com.gender_healthcare_system.dtos" +
            ".ConsultationsDTO(c.consultationId, c.createdAt, " +
            "c.expectedStartTime, c.realStartTime, c.expectedEndTime, c.realEndTime, " +
            "c.status) " +
            "FROM Consultation c " +
            "JOIN c.consultant " +
            "WHERE c.consultant.consultantId = :consultantId")
    Page<ConsultationsDTO> findByConsultantId(int consultantId, Pageable pageable);

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

    @Query("SELECT c.expectedStartTime " +
            "FROM Consultation c " +
            "WHERE c.consultant.consultantId = :id " +
            "AND CAST(c.expectedStartTime as date) = :date")
    List<LocalDateTime> getConsultantScheduleByDate(int id, LocalDate date);

    @Modifying
    @Query("UPDATE Consultation c SET " +
            "c.expectedStartTime = :#{#payload.expectedStartTime}, " +
            "c.expectedEndTime = :expectedEndTime, " +
            "c.status = :status " +
            "WHERE c.consultationId = :consultationId")
    void updateConsultation(int consultationId,
                            @Param("payload") ConsultationConfirmPayload payload,
                            LocalDateTime expectedEndTime,
                            ConsultationStatus status);

    @Modifying
    @Query("UPDATE Consultation c SET " +
            "c.comment = :#{#payload.comment}, " +
            "c.rating = :#{#payload.rating} " +
            "WHERE c.consultationId = :consultationId")
    void updateConsultationCommentAndRatingById(int consultationId,
                            @Param("payload") ConsultationEvaluatePayload payload);

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

    boolean existsConsultationByConsultantConsultantIdAndExpectedStartTime
            (int consultantConsultantId, LocalDateTime expectedStartTime);

}
