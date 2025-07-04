package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.user.StaffDTO;
import com.gender_healthcare_system.entities.user.Staff;
import com.gender_healthcare_system.payloads.user.StaffUpdatePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StaffRepo extends JpaRepository<Staff, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.login.LoginResponse(" +
            "s.staffId, s.fullName, s.email) FROM Staff s " +
            "WHERE s.staffId = :id")
    LoginResponse getStaffLoginDetails(@Param("id") int id);

    @Query("SELECT new com.gender_healthcare_system.dtos.user" +
            ".StaffDTO(s.staffId, a.username, a.password, s.fullName, " +
            "s.phone, s.email, s.address, a.status) " +
            "FROM Staff s " +
            "JOIN s.account a " +
            "WHERE s.staffId = :id")
    Optional<StaffDTO> getStaffDetailsById(int id);

    @Query("SELECT new com.gender_healthcare_system.entities.user.Staff" +
            "(s.staffId, s.fullName, " +
            "s.phone, s.email, s.address) " +
            "FROM Staff s " +
            "WHERE s.staffId = :id")
    Optional<Staff> getStaffDumpById(int id);

    @Query("SELECT COUNT(tsb.serviceBookingId) AS Count, s.staffId " +
            "FROM Staff s " +
            "LEFT JOIN TestingServiceBooking tsb " +
            "ON tsb.staff = s " +
            "AND CAST(tsb.expectedStartTime AS DATE) = :targetDate " +
            "GROUP BY s.staffId " +
            "ORDER BY COUNT(tsb.serviceBookingId) ASC, s.staffId ASC")
    List<Object[]> findStaffOrderedByLeastTests(
            @Param("targetDate") LocalDate targetDate);

    @Query("SELECT new com.gender_healthcare_system.dtos.user" +
            ".StaffDTO(s.staffId, a.username, a.password, s.fullName, " +
            "s.phone, s.email, s.address, a.status) " +
            "FROM Staff s " +
            "JOIN s.account a")
    Page<StaffDTO> getAllStaffs(Pageable pageable);

    // Update staff details (không cập nhật username/password ở đây vì nằm ở Account)
    @Modifying
    //@Transactional
    @Query("UPDATE Staff s SET s.fullName = :#{#payload.fullName}, " +
            "s.email = :#{#payload.email}, " +
            "s.phone = :#{#payload.phone}, " +
            "s.address = :#{#payload.address} " +
            "WHERE s.staffId = :id")
    void updateStaffDetails(@Param("id") int id, @Param("payload") StaffUpdatePayload payload);

    // Delete staff by ID
    @Modifying
    //@Transactional
    @Query("DELETE FROM Staff s WHERE s.staffId = :id")
    void deleteStaffById(@Param("id") int id);
}


