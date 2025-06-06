package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.entities.user.Account;
import com.gender_healthcare_system.entities.user.Staff;
import com.gender_healthcare_system.payloads.StaffPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface StaffRepo extends JpaRepository<Staff, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.LoginResponse(" +
            "s.staffId, s.fullName, s.email) FROM Staff s " +
            "WHERE s.staffId = :id")
    LoginResponse getStaffLoginDetails(@Param("id") int id);

    // Update staff details (không cập nhật username/password ở đây vì nằm ở Account)
    @Modifying
    @Transactional
    @Query("UPDATE Staff s SET s.fullName = :#{#payload.fullName}, s.email = :#{#payload.email}, " +
            "s.phone = :#{#payload.phone}, s.address = :#{#payload.address} WHERE s.staffId = :id")
    void updateStaffDetails(@Param("id") int id, @Param("payload") StaffPayload payload);


    //Cập nhật username và password của tài khoản staff
    @Modifying
    @Query("UPDATE Account s SET s.username = :username, s.password = :password WHERE s.accountId = :accountId")
    void updateStaffAccountCredentials(@Param("accountId") int accountId,
                                       @Param("username") String username,
                                       @Param("password") String password);

    // Delete staff by ID
    @Modifying
    @Transactional
    @Query("DELETE FROM Staff s WHERE s.staffId = :id")
    void deleteStaffById(@Param("id") int id);
}


