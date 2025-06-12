package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.entities.todo.TestingServiceForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestingServiceFormRepo extends JpaRepository<TestingServiceForm, Integer> {

    @Modifying
    @Query("UPDATE TestingServiceForm tsf " +
            "SET tsf.content = :newContent " +
            "WHERE tsf.serviceFormId = :id")
    void updateContentById(int id, String newContent);

    @Modifying
    @Query("DELETE FROM TestingServiceForm tsf WHERE tsf.serviceFormId = :id")
    void deleteByServiceFormId(@Param("id") int id);
}
