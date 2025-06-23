package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.dtos.todo.PriceListDTO;
import com.gender_healthcare_system.entities.enu.PriceListStatus;
import com.gender_healthcare_system.entities.todo.PriceList;
import com.gender_healthcare_system.payloads.todo.PriceListRegisterPayload;
import com.gender_healthcare_system.payloads.todo.PriceListUpdatePayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceListRepo extends JpaRepository<PriceList, Integer> {

    @Query("SELECT new com.gender_healthcare_system.dtos.todo.PriceListDTO" +
            "(pl.priceId, pl.price, pl.description) " +
            "FROM PriceList pl " +
            "JOIN pl.testingService ts " +
            "WHERE ts.serviceId = :id " +
            "AND pl.status = :status")
    List<PriceListDTO> getPriceListForTestingService(int id, PriceListStatus status);

    @Modifying
    @Query("UPDATE PriceList p " +
            "SET p.price = :#{#payload.price}, " +
            "p.description = :#{#payload.description}, " +
            "p.status = :#{#payload.status} " +
            "WHERE p.priceId = :id")
    void updatePriceListById(@Param("id") int id,
                             @Param("payload") PriceListUpdatePayload payload);

    @Modifying
    @Query("DELETE FROM PriceList p " +
            "WHERE p.priceId = :id")
    void deleteByPriceId(@Param("id") int id);

}
