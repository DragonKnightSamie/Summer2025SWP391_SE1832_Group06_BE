package com.gender_healthcare_system.services;

import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.CertificateUpdatePayload;
import com.gender_healthcare_system.repositories.CertificateRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CertificateService {

    private CertificateRepo certificateRepo;

    @Transactional
    public void updateConsultantCertificate(int certificateId, CertificateUpdatePayload payload){
        boolean certificateExist = certificateRepo.existsById(certificateId);

        if(!certificateExist){

            throw new AppException(404, "Certificate not found");
        }

        certificateRepo.updateCertificateById(certificateId, payload);

    }
}
