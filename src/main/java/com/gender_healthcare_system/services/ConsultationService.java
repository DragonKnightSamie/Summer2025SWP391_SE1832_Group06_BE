package com.gender_healthcare_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gender_healthcare_system.dtos.todo.ConsultationDTO;
import com.gender_healthcare_system.dtos.todo.ConsultantScheduleDTO;
import com.gender_healthcare_system.dtos.user.CustomerPeriodDetailsDTO;
import com.gender_healthcare_system.entities.enu.*;
import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.entities.todo.ConsultationPayment;
import com.gender_healthcare_system.entities.user.Account;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.todo.ConsultationCompletePayload;
import com.gender_healthcare_system.payloads.todo.ConsultationConfirmPayload;
import com.gender_healthcare_system.payloads.todo.EvaluatePayload;
import com.gender_healthcare_system.payloads.todo.ConsultationRegisterPayload;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.ConsultationPaymentRepo;
import com.gender_healthcare_system.repositories.ConsultationRepo;
import com.gender_healthcare_system.utils.UtilFunctions;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import com.gender_healthcare_system.entities.enu.AccountStatus;

@Service
@AllArgsConstructor
public class ConsultationService {

    private final AccountRepo accountRepo;
    private final ConsultationRepo consultationRepo;
    private final ConsultationPaymentRepo consultationPaymentRepo;

    //getConsultationById
    public ConsultationDTO getConsultationById(int id)
            throws JsonProcessingException {

        return consultationRepo
                .getConsultationDetailsById(id)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));
    }

    public ConsultationDTO getConsultationByIdForCustomer(int id) {

        return consultationRepo
                .getConsultationDetailsByIdForCustomer(id)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));
    }

    public List<String> getAllConsultationTypesForCustomer(){
        return Arrays.stream(ConsultationType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
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


        Page<ConsultationDTO> pageResult =
                consultationRepo.findByCustomerId(customerId, pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Consultations found");
        }

        List<ConsultationDTO> consultationList = pageResult.getContent();

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


        Page<ConsultationDTO> pageResult =
                consultationRepo.findByConsultantId(consultantId, pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Consultations found");
        }

        List<ConsultationDTO> consultationList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();
        map.put("totalItems", pageResult.getTotalElements());
        map.put("consultations", consultationList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
    }

    public ConsultantScheduleDTO getConsultantScheduleByDate
            (int consultantId, LocalDate date) {

        boolean consultantExist = accountRepo.existsByAccountIdAndRole_RoleNameAndStatus(consultantId, "CONSULTANT", AccountStatus.ACTIVE);

        if(!consultantExist){

            throw new AppException(404, "Consultant not found with ID "+ consultantId);
        }

        List<LocalDateTime> consultationList = consultationRepo
                        .getConsultantScheduleByDate(consultantId, date);

        return new ConsultantScheduleDTO(consultantId, consultationList);
    }

    //register new consultation with payment
    public void registerConsultation(ConsultationRegisterPayload payload) {

        boolean consultationExist = consultationRepo
                .existsConsultationByConsultantAccountIdAndExpectedStartTime
                        (payload.getConsultantId(), payload.getExpectedStartTime());

        if(consultationExist){

            throw new AppException(409, "Consultation has already been booked");
        }

        Account customer = accountRepo.findById(payload.getCustomerId())
                .orElseThrow(() -> new AppException(404,
                        "Customer not found with ID " + payload.getCustomerId()));

        Account consultant = accountRepo.findById(payload.getConsultantId())
                .orElseThrow(() -> new AppException(404,
                        "Consultant not found with ID "+ payload.getConsultantId()));

        Consultation consultation = new Consultation();

        consultation.setCreatedAt(UtilFunctions.getCurrentDateTimeWithTimeZone());
        consultation.setExpectedStartTime(payload.getExpectedStartTime());
        LocalDateTime expectedEndTime = payload.getExpectedStartTime().plusHours(1);
        consultation.setExpectedEndTime(expectedEndTime);
        consultation.setConsultationType(payload.getConsultationType());
        consultation.setStatus(ConsultationStatus.CONFIRMED);
        consultation.setCustomer(customer);
        consultation.setConsultant(consultant);

        consultationRepo.saveAndFlush(consultation);

        ConsultationPayment consultationPayment = new ConsultationPayment();

        consultationPayment.setConsultation(consultation);
        consultationPayment.setAmount(payload.getPayment().getAmount());
        consultationPayment.setMethod(payload.getPayment().getMethod());
        consultationPayment.setStatus(PaymentStatus.PAID);

        consultationPaymentRepo.saveAndFlush(consultationPayment);
    }

    @Transactional
    public void cancelConsultation(int id) {
        boolean consultationExist = consultationRepo.existsById(id);

        if(!consultationExist){

            throw new AppException(404, "Consultation not found");
        }

        consultationRepo.cancelConsultation(id);
    }

    @Transactional
    public void reScheduleConsultation(int consultationId, ConsultationConfirmPayload payload) {

        Consultation consultation = consultationRepo
                .findConsultationById(consultationId)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));

        if (consultation.getStatus() == ConsultationStatus.COMPLETED
                || consultation.getStatus() == ConsultationStatus.CANCELLED) {

            throw new AppException
                    (400, "Cannot reschedule a completed or cancelled consultation");
        }

        boolean consultationTimeConflict = consultationRepo
                .existsConsultationByConsultantAccountIdAndExpectedStartTime
                        (payload.getConsultantId(), payload.getExpectedStartTime());

        if(consultationTimeConflict){

            throw new AppException(409, "Consultation time conflict");
        }

        LocalDateTime expectedEndTime = payload.getExpectedStartTime().plusHours(1);

        consultationRepo.updateConsultation
                (consultationId, payload, expectedEndTime, ConsultationStatus.RESCHEDULED);
    }

    @Transactional
    public void updateConsultationCommentAndRating
            (int id, EvaluatePayload payload) {
        boolean consultationExist = consultationRepo.existsById(id);

        if(!consultationExist){

            throw new AppException(404, "Consultation not found");
        }

        consultationRepo.updateConsultationCommentAndRatingById(id, payload);
    }

    @Transactional
    public void completeConsultation(ConsultationCompletePayload payload) {

        Consultation consultation = consultationRepo
                .findConsultationById(payload.getConsultationId())
                .orElseThrow(() -> new AppException(404, "Consultation not found with ID " +
                        payload.getConsultationId()));

        ConsultationStatus consultationStatus = consultation.getStatus();

        if (consultationStatus == ConsultationStatus.COMPLETED
                || consultationStatus == ConsultationStatus.CANCELLED) {

            throw new AppException
                    (400, "Consultation is already completed " +
                            "or has been cancelled");
        }

        boolean validateRealStartTime =
                payload.getRealStartTime().isBefore(consultation.getExpectedStartTime())
                        || payload.getRealStartTime().isAfter(consultation.getExpectedEndTime())
                        || payload.getRealStartTime().isEqual(consultation.getExpectedEndTime()) ;

        if(validateRealStartTime){

            throw new AppException(400, "Real Start Time cannot be " +
                    "before Expected Start Time or equal to or after Expected End Time");
        }

        boolean validateRealEndTime =
                payload.getRealEndTime().isBefore(consultation.getExpectedStartTime())
                        || payload.getRealEndTime().isAfter(consultation.getExpectedEndTime())
                        || payload.getRealEndTime().isEqual(consultation.getExpectedStartTime()) ;

        if(validateRealEndTime){

            throw new AppException(400, "Real End Time cannot be " +
                    "before or equal to Expected Start Time or after Expected End Time");
        }

        long startAndEndTimeDifference =
                ChronoUnit.MINUTES.between(payload.getRealStartTime(), payload.getRealEndTime());

        if(startAndEndTimeDifference < 20){

            throw new AppException(400,
                    "Real End time must be at least 20 minute later " +
                            "compared to Real Start time");
        }

        consultationRepo.completeConsultation(payload, ConsultationStatus.COMPLETED);
    }
}
