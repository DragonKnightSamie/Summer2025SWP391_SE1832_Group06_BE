package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.repositories.ManagerRepo;
import com.gender_healthcare_system.repositories.StaffRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ManagerService {

    private final ManagerRepo managerRepo;
    private final StaffRepo staffRepo;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse getManagerLoginDetails(int id){
        return managerRepo.getManagerLoginDetails(id);
    }

}
