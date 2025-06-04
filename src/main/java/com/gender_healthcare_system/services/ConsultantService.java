package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.repositories.ConsultantRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsultantService {

    private final ConsultantRepo consultantRepo;

    public LoginResponse getConsultantLoginDetails(int id){
        return consultantRepo.getConsultantLoginDetails(id);
    }
}
