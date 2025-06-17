package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.user.ManagerDTO;
import com.gender_healthcare_system.entities.user.Manager;
import com.gender_healthcare_system.payloads.user.ManagerUpdatePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ManagerRepo extends JpaRepository<Manager, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.LoginResponse(" +
            "m.managerId, m.fullName, m.email) FROM Manager m " +
            "WHERE m.managerId = :id")
    LoginResponse getManagerLoginDetails(int id);

    @Query("SELECT new com.gender_healthcare_system.entities.user.Manager" +
            "(m.managerId, m.fullName, m.phone, m.email, m.address) " +
            "FROM Manager m " +
            "WHERE m.managerId = :id")
    Optional<Manager> getManagerById(int id);

    @Query("SELECT new com.gender_healthcare_system.dtos.ManagerDTO" +
            "(m.managerId, a.username, a.password, " +
            "m.fullName, m.phone, m.email, m.address, a.status) " +
            "FROM Manager m " +
            "JOIN m.account a " +
            "WHERE m.managerId = :id")
    Optional<ManagerDTO> getManagerDetailsById(int id);

    @Query("SELECT new com.gender_healthcare_system.dtos.ManagerDTO" +
            "(m.managerId, a.username, a.password, " +
            "m.fullName, m.phone, m.email, m.address, a.status) " +
            "FROM Manager m " +
            "JOIN m.account a")
    Page<ManagerDTO> getAllManagers(Pageable pageable);

    @Modifying
    @Query("UPDATE Manager m " +
            "SET m.fullName = :#{#payload.fullName}, " +
            "m.phone = :#{#payload.phone}, " +
            "m.email = :#{#payload.email}, " +
            "m.address = :#{#payload.address} " +
            "WHERE m.managerId = :id")
    void updateManagerById(int id, @Param("payload") ManagerUpdatePayload payload);

    @Modifying
    @Query("DELETE FROM Manager m " +
            "WHERE m.managerId = :id")
    void deleteManagerById(int id);


}
