package com.gender_healthcare_system.repositories;

import com.gender_healthcare_system.entities.todo.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepo extends JpaRepository<Consultation, Integer> {
    List<Consultation> findByCustomer_CustomerId(int customerCustomerId);
    List<Consultation> findByConsultant_ConsultantId(int consultantConsultantId);
}
