package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.TestingServiceDTO;
import com.gender_healthcare_system.dtos.TestingServiceHistoryDTO;
import com.gender_healthcare_system.entities.todo.TestingService;
import com.gender_healthcare_system.entities.todo.TestingServiceHistory;
import com.gender_healthcare_system.payloads.TestingServiceHistoryPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestingServiceHistoryRepo extends JpaRepository<TestingServiceHistory, Integer> {

    //get testingServiceHistoryDTO by id
    @Query("SELECT new com.gender_healthcare_system.dtos.TestingServiceHistoryDTO(" +
            "tsh.serviceHistoryId,tsh.result,tsh.rating,tsh.comment,tsh.createdAt,tsh.status, " +
            "new com.gender_healthcare_system.dtos.TestingServiceDTO(ts.serviceId,ts.serviceName,ts.description,ts.status), " +
            "new com.gender_healthcare_system.dtos.StaffDTO(s.staffId, s.fullName, s.phone, s.email,s.address), " +
            "new com.gender_healthcare_system.dtos.CustomerDTO(c.customerId,c.fullName,c.dateOfBirth,c.gender,c.genderSpecificDetails,c.phone,c.email,c.address), " +
            "new com.gender_healthcare_system.dtos.PaymentDTO(p.paymentId, p.amount, p.method,p.status)) " +
            "FROM TestingServiceHistory tsh " +
            "LEFT JOIN tsh.testingService ts " +
            "LEFT JOIN tsh.staff s " +
            "LEFT JOIN tsh.customer c " +
            "LEFT JOIN tsh.payment p " +
            "WHERE tsh.serviceHistoryId = :id")
    Optional<TestingServiceHistoryDTO> getTestingServiceHistoryById(@Param("id") int id);

    // Get a single TestingServices by id (only entity)
    @Query("SELECT tsh " +
            "FROM TestingServiceHistory tsh " +
            "LEFT JOIN tsh.testingService ts " +
            "LEFT JOIN tsh.staff s " +
            "LEFT JOIN tsh.customer c " +
            "LEFT JOIN tsh.payment p " +
            "WHERE tsh.serviceHistoryId = :id")
    Optional<TestingServiceHistory> getTestingServiceHistory(int id);

    //get all TestingServiceHistory (only entity)
    @Query("SELECT tsh " +
            "FROM TestingServiceHistory tsh " +
            "LEFT JOIN tsh.testingService ts " +
            "LEFT JOIN tsh.staff s " +
            "LEFT JOIN tsh.customer c " +
            "LEFT JOIN tsh.payment p")
    Page<TestingServiceHistoryDTO> getAllTestingServiceHistory(Pageable pageable);

    @Modifying
    @Query("UPDATE TestingServiceHistory tsh SET " +
            "tsh.result = :#{#payload.result}, " +
            "tsh.rating = :#{#payload.rating}, " +
            "tsh.comment = :#{#payload.comment}, " +
            "tsh.status = :#{#payload.status} " +
            "WHERE tsh.serviceHistoryId = :id")
    void updateTestingServiceHistory(@Param("id") int id, @Param("payload") TestingServiceHistoryPayload payload);

    @Modifying
    @Query("DELETE FROM TestingServiceHistory tsh WHERE tsh.serviceHistoryId = :id")
    void deleteTestingServiceHistory(@Param("id") int id);
}