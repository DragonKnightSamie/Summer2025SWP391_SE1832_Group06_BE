package com.gender_healthcare_system.iservices;

import com.gender_healthcare_system.entities.user.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConsultantService extends JpaRepository<Consultant,Integer> {

}
