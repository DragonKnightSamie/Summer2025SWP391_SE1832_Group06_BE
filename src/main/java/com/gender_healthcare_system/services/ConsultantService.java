package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.*;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.ConsultantUpdatePayload;
import com.gender_healthcare_system.repositories.CertificateRepo;
import com.gender_healthcare_system.repositories.ConsultantRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class ConsultantService {

    private final ConsultantRepo consultantRepo;

    private final CertificateRepo certificateRepo;

    public LoginResponse getConsultantLoginDetails(int id){
        return consultantRepo.getConsultantLoginDetails(id);
    }

    public List<ConsultantsDTO> getAllConsultants() {
        return consultantRepo.getAllConsultants();
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

        if(!consultantExist){

            throw new AppException(404, "Consultant not found");
        }

        consultantRepo.updateConsultant(consultantId, payload);
    }
}
