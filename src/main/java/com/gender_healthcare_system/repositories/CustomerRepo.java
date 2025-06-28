package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.user.CustomerDTO;
import com.gender_healthcare_system.entities.enu.Gender;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.payloads.user.CustomerUpdatePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.login.LoginResponse(" +
            "c.customerId, c.fullName, c.email) FROM Customer c " +
            "WHERE c.customerId = :id")
    LoginResponse getCustomerLoginDetails(int id);

    @Query("SELECT new com.gender_healthcare_system.entities.user.Customer" +
            "(c.customerId, c.fullName, c.dateOfBirth, c.gender," +
            "c.genderSpecificDetails, c.phone, c.email, c.address) " +
            "FROM Customer c " +
            "WHERE c.customerId = :id")
    Optional<Customer> getCustomerById(int id);

    @Query("SELECT new com.gender_healthcare_system.dtos.user.CustomerDTO" +
            "(c.customerId, a.username, a.password, c.fullName, c.dateOfBirth, c.gender," +
            "c.genderSpecificDetails, c.phone, c.email, c.address, a.status) " +
            "FROM Customer c " +
            "JOIN c.account a " +
            "WHERE c.customerId = :id")
    Optional<CustomerDTO> getCustomerDetailsById(int id);

    @Query("SELECT c.gender " +
            "FROM Customer c " +
            "JOIN c.testingServiceBookings tsh " +
            "WHERE tsh.serviceBookingId = :id")
    Gender getCustomerGenderByBookingId(int id);

    @Query("SELECT new com.gender_healthcare_system.dtos.user.CustomerDTO" +
            "(c.customerId, a.username, c.fullName, c.gender, a.status) " +
            "FROM Customer c " +
            "JOIN c.account a")
    Page<CustomerDTO> getAllCustomers(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Customer c " +
            "WHERE c.customerId = :id")
    void deleteCustomerById(int id);

    @Modifying
    @Query("UPDATE Customer c " +
            "SET c.fullName = :#{#payload.fullName}, " +
            "c.phone = :#{#payload.phone}, " +
            "c.email = :#{#payload.email}, " +
            "c.address = :#{#payload.address}, " +
            "c.genderSpecificDetails = :genderDetails " +
            "WHERE c.customerId = :id")
    void updateCustomerById(int id, @Param("payload")CustomerUpdatePayload payload
            , String genderDetails);
}
