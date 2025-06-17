package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.todo.StaffServiceBookingListDTO;
import com.gender_healthcare_system.dtos.todo.TestingServiceBookingDTO;
import com.gender_healthcare_system.dtos.todo.CustomerServiceBookingListDTO;
import com.gender_healthcare_system.entities.enu.TestingServiceBookingStatus;
import com.gender_healthcare_system.entities.todo.TestingServiceBooking;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestingServiceBookingRepo extends JpaRepository<TestingServiceBooking, Integer> {

    //get testingServiceHistoryDTO by id
    @Query("SELECT new com.gender_healthcare_system.dtos.todo.TestingServiceBookingDTO(" +
            "tsb.serviceBookingId, ts.serviceName, s.fullName, c.fullName, " +
            "tsb.result, tsb.rating, tsb.comment,tsb.createdAt, tsb.expectedStartTime, " +
            "tsb.realStartTime, tsb.expectedEndTime, tsb.realEndTime, tsb.status, " +
            "new com.gender_healthcare_system.dtos.todo.TestingServiceBookingPaymentDTO" +
            "(tsp.amount, tsp.method, tsp.status)) " +
            "FROM TestingServiceBooking tsb " +
            "JOIN tsb.testingService ts " +
            "LEFT JOIN tsb.staff s " +
            "JOIN tsb.customer c " +
            "LEFT JOIN tsb.testingServicePayment tsp " +
            "WHERE tsb.serviceBookingId = :id")
    Optional<TestingServiceBookingDTO> getTestingServiceBookingDetailsById(@Param("id") int id);

    @Query("SELECT new com.gender_healthcare_system.entities.todo.TestingServiceBooking(" +
            "tsb.serviceBookingId, tsb.result, tsb.rating, tsb.comment, " +
            "tsb.createdAt, tsb.expectedStartTime, tsb.realStartTime, tsb.expectedEndTime, " +
            "tsb.realEndTime, tsb.status) " +
            "FROM TestingServiceBooking tsb " +
            "WHERE tsb.serviceBookingId = :id")
    Optional<TestingServiceBooking> getTestingServiceBookingStatusById(@Param("id") int id);


    //get all TestingServiceBooking (only entity)
    @Query("SELECT new com.gender_healthcare_system.dtos.todo.CustomerServiceBookingListDTO" +
            "(tsb.serviceBookingId, ts.serviceName, s.fullName, tsb.createdAt, tsb.status) " +
            "FROM TestingServiceBooking tsb " +
            "JOIN tsb.testingService ts " +
            "LEFT JOIN tsb.staff s " +
            "JOIN tsb.customer c " +
            "WHERE c.customerId = :id")
    Page<CustomerServiceBookingListDTO> getAllTestingServiceBookingsByCustomerId
    (int id, Pageable pageable);

    //get all TestingServiceBooking (only entity)
    @Query("SELECT new com.gender_healthcare_system.dtos.todo.CustomerServiceBookingListDTO" +
            "(tsb.serviceBookingId, ts.serviceName, c.fullName, tsb.createdAt, tsb.status) " +
            "FROM TestingServiceBooking tsb " +
            "JOIN tsb.testingService ts " +
            "LEFT JOIN tsb.staff s " +
            "JOIN tsb.customer c " +
            "WHERE s.staffId = :id")
    Page<StaffServiceBookingListDTO> getAllTestingServiceBookingsByStaffId
    (int id, Pageable pageable);

    @Modifying
    @Query("UPDATE TestingServiceBooking tsb SET " +
            "tsb.result = :#{#payload.result}, " +
            "tsb.rating = :#{#payload.rating}, " +
            "tsb.comment = :#{#payload.comment}, " +
            "tsb.status = :#{#payload.status} " +
            "WHERE tsb.serviceBookingId = :id")
    void updateTestingServiceBooking(@Param("id") int id, @Param("payload") TestingServiceBookingPayload payload);

    @Modifying
    @Query("UPDATE TestingServiceBooking tsb " +
            "SET tsb.status = :status " +
            "WHERE tsb.serviceBookingId = :id")
    void cancelTestingServiceBooking(@Param("id") int id,
                                     TestingServiceBookingStatus status);

    @Modifying
    @Query("DELETE FROM TestingServiceBooking tsb WHERE tsb.serviceBookingId = :id")
    void deleteTestingServiceBooking(@Param("id") int id);
}