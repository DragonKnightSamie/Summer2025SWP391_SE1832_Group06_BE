package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.user.StaffDTO;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.user.StaffUpdatePayload;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.StaffRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class StaffService  {

    private final StaffRepo staffRepo;

    private final AccountRepo accountRepo;

    public LoginResponse getStaffLoginDetails(int id){
        return staffRepo.getStaffLoginDetails(id);
    }

    //getStaffById
    public StaffDTO getStaffById(int id) {
        return staffRepo.getStaffDetailsById(id)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));
    }

    //getConsultationByCustomerId
    public Map<String, Object> getAllStaffs(int page, String sortField, String sortOrder) {

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);

        if(sortOrder.equals("desc")){
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageRequest = PageRequest
                .of(page, itemSize, sort);


        Page<StaffDTO> pageResult = staffRepo.getAllStaffs(pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Staffs found");
        }

        List<StaffDTO> staffList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();

        map.put("totalItems", pageResult.getTotalElements());
        map.put("staffs", staffList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
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
