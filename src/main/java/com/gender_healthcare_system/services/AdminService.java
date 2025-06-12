package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.ManagerDTO;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.repositories.ManagerRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final ManagerRepo managerRepo;

}
