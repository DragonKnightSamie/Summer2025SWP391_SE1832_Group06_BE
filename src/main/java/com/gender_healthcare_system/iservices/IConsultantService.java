package com.gender_healthcare_system.iservices;

import com.gender_healthcare_system.entities.user.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IConsultantService extends JpaRepository<Consultant,Integer> {

}
