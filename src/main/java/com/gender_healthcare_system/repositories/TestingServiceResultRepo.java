package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.TestingServiceResultDTO;
import com.gender_healthcare_system.entities.todo.TestingServiceResult;
import com.gender_healthcare_system.payloads.TestingServiceResultPayload;
import com.gender_healthcare_system.payloads.TestingServiceTypeUpdatePayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestingServiceResultRepo extends JpaRepository<TestingServiceResult, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.TestingServiceResultDTO" +
            "(tst.serviceResultId, tst.title, tst.description) " +
            "FROM TestingServiceResult tst " +
            "WHERE tst.testingServiceType.serviceTypeId = :id")
    List<TestingServiceResultDTO> getAllServiceResultsByTypeId(int id);

    @Modifying
    @Query("UPDATE TestingServiceResult tsr " +
            "SET tsr.title = :#{#payload.title}, " +
            "tsr.description = :#{#payload.description} " +
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
