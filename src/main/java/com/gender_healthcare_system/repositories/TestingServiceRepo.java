package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.todo.TestingServiceDTO;
import com.gender_healthcare_system.dtos.todo.TestingServiceListDTO;
import com.gender_healthcare_system.entities.todo.TestingService;
import com.gender_healthcare_system.payloads.todo.TestingServiceUpdatePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestingServiceRepo extends JpaRepository<TestingService, Integer> {

    // Get a single TestingServiceDTO by ID
    @Query("SELECT new com.gender_healthcare_system.dtos.TestingServiceDTO(" +
            "ts.serviceId, ts.serviceName, ts.description, ts.status, " +
            "new com.gender_healthcare_system.dtos.TestingServiceTypeDTO(tst.serviceTypeId, tst.serviceTypeName, tst.title, tst.content, tst.createdAt), " +
            "new com.gender_healthcare_system.dtos.PriceListDTO(p.priceId, p.price, p.description)) " +
            "FROM TestingService ts " +
            "LEFT JOIN ts.testingServiceType tst " +
            "LEFT JOIN ts.priceLists p " +
            "WHERE ts.serviceId = :id")
    Optional<TestingServiceDTO> getTestingServiceById(@Param("id") int id);

    // Get a single TestingServices by id (only entity)
    @Query("SELECT new com.gender_healthcare_system.entities.todo.TestingService" +
            "(ts.serviceId, ts.serviceName, ts.description, ts.status) " +
            "FROM TestingService ts " +
            "WHERE ts.serviceId = :id")
    Optional<TestingService> getTestingService(int id);

    // Get all TestingServices (only entity)
    @Query("SELECT new com.gender_healthcare_system.dtos.TestingServiceListDTO" +
            "(ts.serviceId, ts.serviceName, tst.serviceTypeName, ts.description, ts.status) " +
            "FROM TestingService ts " +
            "JOIN ts.testingServiceType tst")
    Page<TestingServiceListDTO> getAllTestingServices(Pageable pageable);

    // Update TestingService
    @Modifying
    //@Transactional
    @Query("UPDATE TestingService ts SET ts.serviceName = :#{#payload.serviceName}, " +
            "ts.description = :#{#payload.description}, " +
            "ts.status = :#{#payload.status} " +
            "WHERE ts.serviceId = :id")
    void updateTestingService(@Param("id") int id,
                              @Param("payload") TestingServiceUpdatePayload payload);
}
