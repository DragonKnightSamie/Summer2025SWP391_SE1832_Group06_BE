package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.todo.MenstrualCycleDTO;
import com.gender_healthcare_system.payloads.todo.MenstrualCycleUpdatePayload;
import com.gender_healthcare_system.entities.todo.MenstrualCycle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenstrualCycleRepo extends JpaRepository<MenstrualCycle, Long> {

    // SELECT DTO by customerId
    @Query("SELECT new com.gender_healthcare_system.dtos.todo.MenstrualCycleDTO(" +
            "m.cycleId, m.startDate, m.cycleLength, m.isTrackingEnabled, " +
            "m.createdAt, m.updatedAt, m.customer) " +
            "FROM MenstrualCycle m " +
            "WHERE m.customer.customerId = :customerId")
    List<MenstrualCycleDTO> getCyclesByCustomerId(@Param("customerId") int customerId);

    // UPDATE cycle using payload
    @Transactional
    @Modifying
    @Query("UPDATE MenstrualCycle m SET " +
            "m.startDate = :#{#payload.startDate}, " +
            "m.cycleLength = :#{#payload.cycleLength}, " +
            "m.isTrackingEnabled = :#{#payload.isTrackingEnabled}, " +
            "m.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE m.cycleId = :cycleId")
    void updateCycleById(@Param("cycleId") Long cycleId,
                         @Param("payload") MenstrualCycleUpdatePayload payload);
}
