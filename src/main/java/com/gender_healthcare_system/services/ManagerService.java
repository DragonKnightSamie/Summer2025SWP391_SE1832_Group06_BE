package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.LoginResponse;
import com.gender_healthcare_system.dtos.ManagerDTO;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.ManagerUpdatePayload;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.ManagerRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ManagerService {

    private final ManagerRepo managerRepo;

    private final AccountRepo accountRepo;

    public LoginResponse getManagerLoginDetails(int id){
        return managerRepo.getManagerLoginDetails(id);
    }

    public ManagerDTO getManagerDetails(int id){
        return managerRepo.getManagerDetailsById(id)
                .orElseThrow(() -> new AppException
                        (404, "Manager not found with ID "+ id));
    }

    public Map<String, Object> getAllManagers(int page, String sortField, String sortOrder){

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);

        if(sortOrder.equals("desc")){
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageRequest = PageRequest
                .of(page, itemSize, sort);


        Page<ManagerDTO> pageResult = managerRepo.getAllManagers(pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Managers found");
        }

        List<ManagerDTO> managerList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();

        map.put("totalItems", pageResult.getTotalElements());
        map.put("managers", managerList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
    }

    @Transactional
    public void updateManagerDetails(int id, ManagerUpdatePayload payload){
        boolean managerExist = managerRepo.existsById(id);

        if(!managerExist){

            throw new AppException(404, "Manager not found with ID " + id);
        }

        boolean accountStatusIdentical = accountRepo
                .existsAccountByAccountIdAndStatus(id, payload.getStatus());

        if(!accountStatusIdentical){

            accountRepo.updateAccountStatus(id, payload.getStatus());
        }

        managerRepo.updateManagerById(id, payload);
    }
}
