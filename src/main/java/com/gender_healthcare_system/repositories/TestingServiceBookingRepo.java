package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.todo.StaffServiceBookingListDTO;
import com.gender_healthcare_system.dtos.todo.TestingServiceBookingDTO;
import com.gender_healthcare_system.dtos.todo.CustomerServiceBookingListDTO;
import com.gender_healthcare_system.entities.enu.TestingServiceBookingStatus;
import com.gender_healthcare_system.entities.todo.TestingServiceBooking;
import com.gender_healthcare_system.entities.user.Staff;
import com.gender_healthcare_system.payloads.todo.EvaluatePayload;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingCompletePayload;

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
            "WHERE s.staffId = :id " +
            "AND NOT tsb.status = :status")
    Page<StaffServiceBookingListDTO> getAllTestingServiceBookingsByStaffId
    (int id, Pageable pageable, TestingServiceBookingStatus status);

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.CustomerServiceBookingListDTO" +
            "(tsb.serviceBookingId, ts.serviceName, c.fullName, tsb.createdAt, tsb.status) " +
            "FROM TestingServiceBooking tsb " +
            "JOIN tsb.testingService ts " +
            "JOIN tsb.customer c " +
            "WHERE NOT tsb.status = :status")
    Page<StaffServiceBookingListDTO> getAllPendingTestingServiceBookings
            (Pageable pageable, TestingServiceBookingStatus status);

    @Query("SELECT tsb.expectedStartTime " +
            "FROM TestingServiceBooking tsb " +
            "JOIN tsb.staff st " +
            "WHERE st.staffId = :staffId " +
            "AND CAST(tsb.expectedStartTime AS DATE) = :date " +
            "AND NOT tsb.status = :status1 " +
            "AND NOT tsb.status = :status2 " +
            "GROUP BY tsb.expectedStartTime " +
            "HAVING COUNT(tsb) = 5 " +
            "ORDER BY tsb.expectedStartTime")
    List<LocalDateTime> getStaffScheduleInADate
            (int staffId, LocalDate date,
             TestingServiceBookingStatus status1,
             TestingServiceBookingStatus status2);

    @Query("SELECT COUNT(tsb) AS numberOfBookings " +
            "FROM TestingServiceBooking tsb " +
            "JOIN tsb.staff st " +
            "WHERE st.staffId = :staffId " +
            "AND tsb.expectedStartTime = :startTime " +
            "AND NOT tsb.status = :status1 " +
            "AND NOT tsb.status = :status2")
    int getNumberOfBookingsAStaffHaveInATime
            (int staffId, LocalDateTime startTime,
             TestingServiceBookingStatus status1,
             TestingServiceBookingStatus status2);

    @Modifying
    @Query("UPDATE TestingServiceBooking tsb SET " +
            "tsb.staff = :staff, " +
            "tsb.expectedStartTime = :startTime, " +
            "tsb.expectedEndTime = :endTime, " +
            "tsb.status = :status " +
            "WHERE tsb.serviceBookingId = :id")
    void confirmTestingServiceBooking(int id, Staff staff,
                                      LocalDateTime startTime, LocalDateTime endTime,
                                      TestingServiceBookingStatus status);

    @Modifying
    @Query("UPDATE TestingServiceBooking tsb SET " +
            "tsb.result = :result, " +
            "tsb.realStartTime = :#{#payload.realStartTime}, " +
            "tsb.realEndTime = :#{#payload.realEndTime}, " +
            "tsb.status = :status " +
            "WHERE tsb.serviceBookingId = :id")
    void completeTestingServiceBooking(@Param("id") int id, @Param("payload")
                        TestingServiceBookingCompletePayload payload, String result,
                        TestingServiceBookingStatus status);

    @Modifying
    @Query("UPDATE TestingServiceBooking tsb SET " +
            "tsb.comment = :#{#payload.comment}, " +
            "tsb.rating = :#{#payload.rating} " +
            "WHERE tsb.serviceBookingId = :bookingId")
    void updateServiceBookingCommentAndRatingById(int bookingId,
                                                @Param("payload") EvaluatePayload payload);

    @Modifying
    @Query("UPDATE TestingServiceBooking tsb " +
            "SET tsb.status = :status " +
            "WHERE tsb.serviceBookingId = :id")
    void cancelTestingServiceBooking(@Param("id") int id,
                                     TestingServiceBookingStatus status);

    @Modifying
    @Query("DELETE FROM TestingServiceBooking tsb WHERE tsb.serviceBookingId = :id")
    void deleteTestingServiceBooking(@Param("id") int id);

    boolean existsByStaffStaffIdAndExpectedStartTime(int staffStaffId, LocalDateTime expectedStartTime);
}