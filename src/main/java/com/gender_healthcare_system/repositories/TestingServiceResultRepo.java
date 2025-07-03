package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.todo.TestingServiceResultDTO;
import com.gender_healthcare_system.entities.enu.Gender;
import com.gender_healthcare_system.entities.enu.GenderType;
import com.gender_healthcare_system.entities.todo.TestingServiceResult;
import com.gender_healthcare_system.payloads.todo.TestingServiceResultPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestingServiceResultRepo extends JpaRepository<TestingServiceResult, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.TestingServiceResultDTO" +
            "(tst.serviceResultId, tst.title, tst.description, tst.type, tst.genderType, " +
            "tst.measureUnit, tst.minValue, tst.maxValue) " +
            "FROM TestingServiceResult tst " +
            "WHERE tst.testingServiceType.serviceTypeId = :id")
    List<TestingServiceResultDTO> getAllServiceResultsByTypeId(int id);

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.TestingServiceResultDTO" +
            "(tst.serviceResultId, tst.title, tst.description, tst.type, tst.genderType, " +
            "tst.measureUnit, tst.minValue, tst.maxValue) " +
            "FROM TestingServiceResult tst " +
            "WHERE tst.testingServiceType.serviceTypeId = " +
            "(SELECT ts.testingServiceType.serviceTypeId " +
            "FROM TestingServiceBooking tsb " +
            "JOIN tsb.testingService ts " +
            "WHERE tsb.serviceBookingId = :id) " +
            "AND tst.genderType <> :genderType")
    List<TestingServiceResultDTO> getAllServiceResultsByTypeIdAndGenderType
            (int id, GenderType genderType);

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.TestingServiceResultDTO" +
            "(tst.serviceResultId, tst.title, tst.description, tst.type, tst.genderType, " +
            "tst.measureUnit, tst.minValue, tst.maxValue) " +
            "FROM TestingServiceResult tst " +
            "WHERE tst.testingServiceType.serviceTypeId = " +
            "(SELECT ts.testingServiceType.serviceTypeId " +
            "FROM TestingService ts " +
            "WHERE ts.serviceId = :serviceId)")
    List<TestingServiceResultDTO> getAllServiceResultsByServiceId(int serviceId);

    @Modifying
    @Query("UPDATE TestingServiceResult tsr " +
            "SET tsr.title = :#{#payload.title}, " +
            "tsr.description = :#{#payload.description}, " +
            "tsr.type = :#{#payload.type}, " +
            "tsr.genderType = :#{#payload.genderType}, " +
            "tsr.measureUnit = :#{#payload.measureUnit}, " +
            "tsr.minValue = :#{#payload.minValue}, " +
            "tsr.maxValue = :#{#payload.maxValue} " +
            "WHERE tsr.serviceResultId = :id")
    void updateTestingServiceResult(int id,
                                  @Param("payload") TestingServiceResultPayload payload);

    @Modifying
    @Query("DELETE FROM TestingServiceResult tsr " +
            "WHERE tsr.serviceResultId = :id")
    void deleteTestingServiceResultById(int id);

    @Modifying
    @Query("DELETE FROM TestingServiceResult tsr " +
            "WHERE tsr.testingServiceType.serviceTypeId = :id")
    void deleteTestingServiceResultByServiceTypeId(int id);

}
