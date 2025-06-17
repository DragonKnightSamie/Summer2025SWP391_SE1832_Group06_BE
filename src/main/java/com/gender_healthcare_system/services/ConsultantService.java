package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.todo.CertificateDTO;
import com.gender_healthcare_system.dtos.todo.ConsultantDetailsDTO;
import com.gender_healthcare_system.dtos.user.ConsultantsDTO;
import com.gender_healthcare_system.dtos.user.ListConsultantDTO;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.user.ConsultantUpdatePayload;
import com.gender_healthcare_system.repositories.CertificateRepo;
import com.gender_healthcare_system.repositories.ConsultantRepo;
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
public class ConsultantService {

    private final ConsultantRepo consultantRepo;

    private final CertificateRepo certificateRepo;

    public LoginResponse getConsultantLoginDetails(int id) {
        return consultantRepo.getConsultantLoginDetails(id);
    }

    //get all consultant
    public List<ListConsultantDTO> getAllConsultantsForCustomer() {
        List<ListConsultantDTO> consultants = consultantRepo.getAllConsultantsForCustomer();
        if (consultants.isEmpty()) {
            throw new AppException(404, "No Consultants found");
        }
        return consultants;
    }

    public Map<String, Object> getAllConsultants(int page, String sortField, String sortOrder) {

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);

        if (sortOrder.equals("desc")) {
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageRequest = PageRequest
                .of(page, itemSize, sort);


        Page<ConsultantsDTO> pageResult = consultantRepo.getAllConsultants(pageRequest);

        if (!pageResult.hasContent()) {

            throw new AppException(404, "No Consultants found");
        }

        List<ConsultantsDTO> consultantList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();
        map.put("totalItems", pageResult.getTotalElements());
        map.put("consultants", consultantList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
    }

    public ConsultantDetailsDTO getConsultantDetails(int consultantId) {
        ConsultantDetailsDTO consultantDetail =
                consultantRepo.getConsultantDetails(consultantId)
                        .orElseThrow(() -> new AppException(404, "Consultant not found"));

        List<CertificateDTO> certificateList =
                certificateRepo.getCertificatesByConsultantId(consultantId);

        consultantDetail.setCertificateList(certificateList);

        return consultantDetail;
    }

    @Transactional
    public void updateConsultantDetails(int consultantId, ConsultantUpdatePayload payload) {
        boolean consultantExist =
                consultantRepo.existsById(consultantId);

        if (!consultantExist) {

            throw new AppException(404, "Consultant not found");
        }

        consultantRepo.updateConsultant(consultantId, payload);
    }
}
