package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.repositories.StaffRepo;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StaffService {

    private final StaffRepo staffRepo;

    public LoginResponse getStaffLoginDetails(int id){
        return staffRepo.getStaffLoginDetails(id);
    }
}
