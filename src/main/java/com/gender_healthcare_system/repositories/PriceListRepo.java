package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.entities.todo.PriceList;
import com.gender_healthcare_system.payloads.todo.PriceListPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceListRepo extends JpaRepository<PriceList, Integer> {

    @Modifying
    @Query("UPDATE PriceList p " +
            "SET p.price = :#{#payload.price}, " +
            "p.description = :#{#payload.description} " +
            "WHERE p.priceId = :id")
    void updatePriceListById(@Param("id") int id, @Param("payload") PriceListPayload payload);

    @Modifying
    @Query("DELETE FROM PriceList p " +
            "WHERE p.priceId = :id")
    void deleteByPriceId(@Param("id") int id);

}
