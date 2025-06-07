package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.dtos.StaffDTO;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.StaffUpdatePayload;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.StaffRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StaffService {

    private final StaffRepo staffRepo;

    private final AccountRepo accountRepo;

    public LoginResponse getStaffLoginDetails(int id){
        return staffRepo.getStaffLoginDetails(id);
    }

    public StaffDTO getStaffById(int id) {
        return staffRepo.getStaffDetailsById(id)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));
    }

    //getConsultationByCustomerId
    public List<StaffDTO> getAllStaffs() {
        return staffRepo.getAllStaffs();
    }

    @Transactional
    public void updateStaffAccount(int staffId, StaffUpdatePayload payload) {
        boolean staffExist = staffRepo.existsById(staffId);

        if(!staffExist) {

                throw new AppException(404, "Staff not found");
        }

        boolean accountStatusIdentical = accountRepo
                .existsAccountByAccountIdAndStatus(staffId, payload.getStatus());

        if(!accountStatusIdentical){

            accountRepo.updateAccountStatus(staffId, payload.getStatus());
        }

        staffRepo.updateStaffDetails(staffId, payload);
    }
}
