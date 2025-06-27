package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.todo.ConsultantConsultationDTO;
import com.gender_healthcare_system.dtos.todo.ConsultantScheduleDTO;
import com.gender_healthcare_system.dtos.todo.ConsultationsDTO;
import com.gender_healthcare_system.entities.enu.ConsultationStatus;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import com.gender_healthcare_system.entities.todo.Consultation;
import com.gender_healthcare_system.entities.todo.ConsultationPayment;
import com.gender_healthcare_system.entities.user.Consultant;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.todo.ConsultationCompletePayload;
import com.gender_healthcare_system.payloads.todo.ConsultationConfirmPayload;
import com.gender_healthcare_system.payloads.todo.EvaluatePayload;
import com.gender_healthcare_system.payloads.todo.ConsultationRegisterPayload;
import com.gender_healthcare_system.repositories.ConsultantRepo;
import com.gender_healthcare_system.repositories.ConsultationPaymentRepo;
import com.gender_healthcare_system.repositories.ConsultationRepo;
import com.gender_healthcare_system.repositories.CustomerRepo;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ConsultationService {

    private final CustomerRepo customerRepo;

    private final ConsultantRepo consultantRepo;

    private final ConsultationRepo consultationRepo;

    private final ConsultationPaymentRepo consultationPaymentRepo;

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

    public ConsultantScheduleDTO getConsultantScheduleByDate
            (int consultantId, LocalDate date) {

        boolean consultantExist = consultantRepo.existsById(consultantId);

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
                .existsConsultationByConsultantConsultantIdAndExpectedStartTime
                        (payload.getConsultantId(), payload.getExpectedStartTime());

        if(consultationExist){

            throw new AppException(409, "Consultation has already been booked");
        }

        Customer customer = customerRepo.getCustomerById(payload.getCustomerId())
                .orElseThrow(() -> new AppException(404,
                        "Customer not found with ID " + payload.getCustomerId()));

        Consultant consultant = consultantRepo.getConsultantById(payload.getConsultantId())
                .orElseThrow(() -> new AppException(404,
                        "Consultant not found with ID "+ payload.getConsultantId()));

        Consultation consultation = new Consultation();

        consultation.setCreatedAt(UtilFunctions.getCurrentDateTimeWithTimeZone());
        consultation.setExpectedStartTime(payload.getExpectedStartTime());
        LocalDateTime expectedEndTime = payload.getExpectedStartTime().plusHours(1);
        consultation.setExpectedEndTime(expectedEndTime);
        consultation.setDescription(payload.getDescription()); //thêm description
        consultation.setStatus(ConsultationStatus.CONFIRMED);
        consultation.setCustomer(customer);
        consultation.setConsultant(consultant);

        consultationRepo.save(consultation);

        ConsultationPayment payment = new ConsultationPayment();

        payment.setConsultation(consultation);
        payment.setTransactionId(payment.getTransactionId());
        payment.setAmount(payload.getPayment().getAmount());
        payment.setCreatedAt(UtilFunctions.getCurrentDateTimeWithTimeZone());
        payment.setMethod(payload.getPayment().getMethod());
        payment.setDescription(payment.getDescription());
        payment.setStatus(PaymentStatus.PAID);

        consultationPaymentRepo.saveAndFlush(payment);
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
    public void reScheduleConsultation(int consultationId, ConsultationConfirmPayload payload) {

        boolean consultationExist = consultationRepo
                .existsConsultationByConsultantConsultantIdAndExpectedStartTime
                        (payload.getConsultantId(), payload.getExpectedStartTime());

        if(consultationExist){

            throw new AppException(409,
                    "This Consultant already has a Consultation " +
                            "with provided expected start time");
        }

        Consultation consultation = consultationRepo
                .findConsultationById(consultationId)
                .orElseThrow(() -> new AppException(404, "Consultation not found"));

        if (consultation.getStatus() == ConsultationStatus.COMPLETED
                || consultation.getStatus() == ConsultationStatus.CANCELLED) {

            throw new AppException
                    (400, "Cannot reschedule a completed or cancelled consultation");
        }

        LocalDateTime expectedEndTime = payload.getExpectedStartTime().plusHours(1);
        /*consultation.setExpectedStartTime(payload.getExpectedStartTime());
        consultation.setExpectedEndTime(payload.getExpectedEndTime());
        consultation.setStatus(ConsultationStatus.RESCHEDULED);

        consultationRepo.save(consultation);*/
        consultationRepo.updateConsultation
                (consultationId, payload, expectedEndTime, ConsultationStatus.RESCHEDULED);
    }

    @Transactional
    public void updateConsultationCommentAndRating
            (int id, EvaluatePayload payload) {

        boolean consultationExist = consultationRepo.existsById(id);

        if(!consultationExist){

            throw new AppException(404,
                    "Consultation not found");
        }

        consultationRepo.updateConsultationCommentAndRatingById
                (id, payload);
    }

    @Transactional
    public void completeConsultation(ConsultationCompletePayload payload) {

        Consultation consultation = consultationRepo
                .findConsultationById(payload.getConsultationId())
                .orElseThrow(() -> new AppException(404, "Consultation not found"));

        ConsultationStatus consultationStatus = consultation.getStatus();

        if (consultationStatus == ConsultationStatus.COMPLETED
                || consultationStatus == ConsultationStatus.CANCELLED) {

            throw new AppException
                    (400, "Cannot mark an already completed " +
                            "or cancelled consultation as completed");
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

        boolean validateStartAndEndTime =
                payload.getRealStartTime().isAfter(payload.getRealEndTime());

        if(validateStartAndEndTime){

            throw new AppException(400, "Real End Time cannot be " +
                    "after real Start Time");
        }
                /* consultation.setRealStartTime(payload.getRealStartTime());
        consultation.setRealEndTime(payload.getRealEndTime());
        consultation.setStatus(ConsultationStatus.COMPLETED);

        consultationRepo.save(consultation);*/
        consultationRepo.completeConsultation(payload, ConsultationStatus.COMPLETED);
    }


}
