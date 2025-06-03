package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.entities.user.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultantRepo extends JpaRepository<Consultant, Integer> {
}
