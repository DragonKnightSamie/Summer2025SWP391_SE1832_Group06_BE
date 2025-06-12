package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.ConsultantConsultationDTO;
import com.gender_healthcare_system.dtos.ConsultationsDTO;
import com.gender_healthcare_system.dtos.TestingServiceListDTO;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.entities.user.Consultant;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.ConsultationCompletePayload;
import com.gender_healthcare_system.payloads.ConsultationConfirmPayload;
import com.gender_healthcare_system.payloads.ConsultationRegisterPayload;
import com.gender_healthcare_system.repositories.ConsultantRepo;
import com.gender_healthcare_system.repositories.ConsultationRepo;
import com.gender_healthcare_system.repositories.CustomerRepo;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ConsultationService {

    private final CustomerRepo customerRepo;
    
    private final ConsultantRepo consultantRepo;

    private final ConsultationRepo consultationRepo;

    //getConsultationById
    public ConsultantConsultationDTO getConsultationById(int id) {
        return consultationRepo.getConsultationDetailsById(id)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));
    }

    //getConsultationByCustomerId
    public Map<String, Object> getConsultationsByCustomerId
    (int customerId, int page, String sortField, String sortOrder) {

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);

        if(sortOrder.equals("desc")){
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageRequest = PageRequest
                .of(page, itemSize, sort);


        Page<ConsultationsDTO> pageResult =
                consultationRepo.findByCustomerId(customerId, pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Consultations found");
        }

        List<ConsultationsDTO> consultationList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();

        map.put("totalItems", pageResult.getTotalElements());
        map.put("consultations", consultationList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
    }

    //getConsultationByConsultantId
    public Map<String, Object> getConsultationsByConsultantId
    (int consultantId, int page, String sortField, String sortOrder) {

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);

        if(sortOrder.equals("desc")){
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageRequest = PageRequest
                .of(page, itemSize, sort);


        Page<ConsultationsDTO> pageResult =
                consultationRepo.findByConsultantId(consultantId, pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Consultations found");
        }

        List<ConsultationsDTO> consultationList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();
        map.put("totalItems", pageResult.getTotalElements());
        map.put("consultations", consultationList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
    }


    //register

    public void registerConsultation(ConsultationRegisterPayload payload) {
        Customer customer = customerRepo.getCustomerById(payload.getCustomerId())
                .orElseThrow(() -> new AppException(404, "Customer not found"));

        Consultant consultant = consultantRepo.getConsultantById(payload.getConsultantId())
                .orElseThrow(() -> new AppException(404, "Consultant not found"));

        Consultation consultation = new Consultation();
        consultation.setCreatedAt(LocalDateTime.now());
        consultation.setExpectedStartTime(payload.getExpectedStartTime());
        //LocalDateTime endtime = payload.getExpectedStartTime().plusMinutes(60);
        consultation.setExpectedEndTime(payload.getExpectedEndTime());
        consultation.setStatus(ConsultationStatus.CONFIRMED);
        consultation.setCustomer(customer);
        consultation.setConsultant(consultant);

        consultationRepo.save(consultation);

    }

    //confirm consultation : consultant xác nhận lịch hẹn
    /*@Transactional
    public void confirmConsultation(ConsultationConfirmPayload payload) {
        Consultation consultation = consultationRepo
                .findConsultationById(payload.getConsultationId())
                .orElseThrow(() -> new AppException(404, "Consultation not found"));

        if (consultation.getStatus() != ConsultationStatus.PENDING) {
            throw new AppException(400, "Only pending consultations can be approved");
        }

        *//*consultation.setStatus(ConsultationStatus.CONFIRMED);
        consultation.setExpectedStartTime(payload.getExpectedStartTime());
        consultation.setExpectedEndTime(payload.getExpectedEndTime());

        consultationRepo.save(consultation);*//*
        consultationRepo.updateConsultation(payload, ConsultationStatus.CONFIRMED);
    }*/

    @Transactional
    public void cancelConsultation(int id) {
        Consultation consultation = consultationRepo.findConsultationById(id)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));

        if (consultation.getStatus() == ConsultationStatus.COMPLETED
                || consultation.getStatus() == ConsultationStatus.CANCELLED ) {
            throw new AppException(400,
                    "Cannot cancel an already cancelled or completed consultation");
        }

        /*consultation.setStatus(ConsultationStatus.CANCELLED);
        consultationRepo.save(consultation);*/
        consultationRepo.cancelConsultation(id);
    }

    @Transactional
    public void reScheduleConsultation(ConsultationConfirmPayload payload) {
        Consultation consultation = consultationRepo
                .findConsultationById(payload.getConsultationId())
                .orElseThrow(() -> new AppException(404, "Consultation not found"));

        if (consultation.getStatus() == ConsultationStatus.COMPLETED
                || consultation.getStatus() == ConsultationStatus.CANCELLED) {

            throw new AppException
                    (400, "Cannot reschedule a completed or cancelled consultation");
        }

        /*consultation.setExpectedStartTime(payload.getExpectedStartTime());
        consultation.setExpectedEndTime(payload.getExpectedEndTime());
        consultation.setStatus(ConsultationStatus.RESCHEDULED);

        consultationRepo.save(consultation);*/
        consultationRepo.updateConsultation(payload, ConsultationStatus.RESCHEDULED);
    }

    @Transactional
    public void completeConsultation(ConsultationCompletePayload payload) {
        Consultation consultation = consultationRepo
                .findConsultationById(payload.getConsultationId())
                .orElseThrow(() -> new AppException(404, "Consultation not found"));

        if (consultation.getStatus() == ConsultationStatus.COMPLETED
                || consultation.getStatus() == ConsultationStatus.CANCELLED) {

            throw new AppException
                    (400, "Cannot mark an already completed " +
                            "or cancelled consultation as completed");
        }

       /* consultation.setRealStartTime(payload.getRealStartTime());
        consultation.setRealEndTime(payload.getRealEndTime());
        consultation.setStatus(ConsultationStatus.COMPLETED);

        consultationRepo.save(consultation);*/
        consultationRepo.completeConsultation(payload, ConsultationStatus.COMPLETED);
    }
}
