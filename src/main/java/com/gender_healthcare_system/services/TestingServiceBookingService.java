package com.gender_healthcare_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gender_healthcare_system.dtos.todo.StaffServiceBookingListDTO;
import com.gender_healthcare_system.dtos.todo.TestingServiceBookingDTO;
import com.gender_healthcare_system.dtos.todo.CustomerServiceBookingListDTO;
import com.gender_healthcare_system.dtos.todo.TestingServiceBookingResultDTO;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import com.gender_healthcare_system.entities.enu.TestingServiceBookingStatus;
import com.gender_healthcare_system.entities.todo.TestingService;
import com.gender_healthcare_system.entities.todo.TestingServiceBooking;
import com.gender_healthcare_system.entities.todo.TestingServicePayment;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.entities.user.Staff;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.todo.EvaluatePayload;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingCompletePayload;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingConfirmPayload;
import com.gender_healthcare_system.payloads.todo.TestingServiceBookingRegisterPayload;
import com.gender_healthcare_system.repositories.*;
import com.gender_healthcare_system.utils.TimeFunctions;
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
public class TestingServiceBookingService {

    private final CustomerRepo customerRepo;

    private final StaffRepo staffRepo;

    private final TestingServiceRepo testingServiceRepo;

    private final TestingServiceBookingRepo testingServiceBookingRepo;

    private final TestingServicePaymentRepo testingServicePaymentRepo;

    // Get TestingServiceBooking entity by id
    public TestingServiceBookingDTO getTestingServiceBookingDetailsById(int id)
            throws JsonProcessingException {
        TestingServiceBookingDTO testingService = testingServiceBookingRepo
                .getTestingServiceBookingDetailsById(id)
                .orElseThrow(() -> new AppException
                        (404, "Testing Service Booking not found with ID: " + id));

        String result = testingService.getResult();

        if(!result.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();

            List<TestingServiceBookingResultDTO> resultList = mapper.readValue(result, new
                    TypeReference<List<TestingServiceBookingResultDTO>>(){});

            testingService.setResults(resultList);
        }

        return testingService;
    }

    // Get all TestingServiceBookings by Customer ID with pagination
    public Map<String, Object> getAllTestingServiceBookingsByCustomerId
    (int customerId, int page, String sortField, String sortOrder) {

        boolean customerExist = customerRepo.existsById(customerId);
        if (!customerExist) {
            throw new AppException(404, "Customer not found with ID: " + customerId);
        }

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);
        if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageable = PageRequest.of(page, itemSize, sort);
        Page<CustomerServiceBookingListDTO> pageResult =
                testingServiceBookingRepo
                        .getAllTestingServiceBookingsByCustomerId(customerId, pageable);

        if (!pageResult.hasContent()) {
            throw new AppException(404, "No Testing Service Bookings found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", pageResult.getTotalElements());
        response.put("testingServiceBookings", pageResult.getContent());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("currentPage", pageResult.getNumber());

        return response;
    }

    // Get all TestingServiceBookings by Staff ID with pagination
    public Map<String, Object> getAllTestingServiceBookingsByStaffId
            (int staffId, int page, String sortField, String sortOrder) {

        boolean staffExist = staffRepo.existsById(staffId);
        if (!staffExist) {
            throw new AppException(404, "Staff not found with ID: " + staffId);
        }

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);
        if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageable = PageRequest.of(page, itemSize, sort);
        Page<StaffServiceBookingListDTO> pageResult =
                testingServiceBookingRepo
                        .getAllTestingServiceBookingsByStaffId
                                (staffId, pageable, TestingServiceBookingStatus.PENDING);

        if (!pageResult.hasContent()) {
            throw new AppException(404, "No Testing Service Bookings found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", pageResult.getTotalElements());
        response.put("testingServiceBookings", pageResult.getContent());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("currentPage", pageResult.getNumber());

        return response;
    }

    // Get all TestingServiceBookings by Staff ID with pagination
    public Map<String, Object> getAllPendingTestingServiceBookings
    (int page, String sortField, String sortOrder) {

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);
        if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageable = PageRequest.of(page, itemSize, sort);

        Page<StaffServiceBookingListDTO> pageResult =
                testingServiceBookingRepo
                        .getAllPendingTestingServiceBookings
                                (pageable, TestingServiceBookingStatus.PENDING);

        if (!pageResult.hasContent()) {
            throw new AppException(404, "No Pending Testing Service Bookings found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", pageResult.getTotalElements());
        response.put("pendingServiceBookings", pageResult.getContent());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("currentPage", pageResult.getNumber());

        return response;
    }

    public List<LocalDateTime> getStaffScheduleForADay
            (int staffId, LocalDate date){

        boolean staffExist = staffRepo
                .existsById(staffId);

        if(!staffExist){
            throw new AppException(400,
                    "No Staff found with ID " + staffId);
        }

        List<LocalDateTime> staffSchedule =
                testingServiceBookingRepo
                        .getStaffScheduleInADate
                                (staffId, date ,
                                        TestingServiceBookingStatus.PENDING,
                                        TestingServiceBookingStatus.CANCELLED);

        if (staffSchedule.isEmpty()) {
            throw new AppException(404, "No Staff Schedule found ");
        }

        return staffSchedule;
    }


    public void createTestingServiceBooking
            (TestingServiceBookingRegisterPayload payload) {

        TestingService testingService = testingServiceRepo
                .getTestingService(payload.getServiceId())
                .orElseThrow(() -> new AppException(400,
                        "Testing Service not found with ID " + payload.getServiceId()));

        Customer customer = customerRepo
                .getCustomerById(payload.getCustomerId())
                .orElseThrow(() -> new AppException(400,
                        "Customer not found with ID " + payload.getCustomerId()));

        TestingServiceBooking serviceBooking = new TestingServiceBooking();

        serviceBooking.setTestingService(testingService);
        serviceBooking.setCustomer(customer);
        serviceBooking.setCreatedAt(TimeFunctions.getCurrentDateTimeWithTimeZone());
        serviceBooking.setStatus(TestingServiceBookingStatus.PENDING);

        testingServiceBookingRepo.saveAndFlush(serviceBooking);

        TestingServicePayment servicePayment = new TestingServicePayment();

        servicePayment.setTestingServiceBooking(serviceBooking);
        servicePayment.setOrderId(payload.getPaymentOrderId());
        servicePayment.setAmount(payload.getPaymentAmount());
        servicePayment.setMethod(payload.getPaymentMethod());
        servicePayment.setCreatedAt(TimeFunctions.getCurrentDateTimeWithTimeZone());
        servicePayment.setDescription(payload.getDescription());
        servicePayment.setStatus(PaymentStatus.PAID);

        testingServicePaymentRepo.saveAndFlush(servicePayment);
    }

    @Transactional
    public void confirmTestingServiceBooking
            (int id, TestingServiceBookingConfirmPayload payload) {

        boolean serviceBookingExist = testingServiceBookingRepo.existsById(id);

        if(!serviceBookingExist) {

            throw new AppException(400,
                    "Testing Service not found with ID " + id);
        }

        Staff staff = staffRepo.getStaffDumpById(payload.getStaffId())
                .orElseThrow(() -> new AppException(404,
                        "No Staff found with ID "+ payload.getStaffId()));

        int numberOfBookingsAStaffHaveInATime =
                testingServiceBookingRepo
                        .getNumberOfBookingsAStaffHaveInATime
                                (payload.getStaffId(), payload.getExpectedStartTime(),
                                        TestingServiceBookingStatus.PENDING,
                                        TestingServiceBookingStatus.CANCELLED);

        if(numberOfBookingsAStaffHaveInATime == 5) {

            throw new AppException(400,
                    "A Service Booking with provided expected Start time " +
                            " has been fully booked");
        }

        LocalDateTime endTime = payload.getExpectedStartTime().plusHours(1);

        testingServiceBookingRepo.confirmTestingServiceBooking
                (id, staff, payload.getExpectedStartTime(), endTime,
                        TestingServiceBookingStatus.CONFIRMED);
    }

    // Update
    @Transactional
    public void completeTestingServiceBooking
    (int id, TestingServiceBookingCompletePayload payload) throws JsonProcessingException {

        TestingServiceBooking serviceBooking =
                testingServiceBookingRepo.getTestingServiceBookingStatusById(id)
                        .orElseThrow(() ->new AppException(404,
                                "Testing Service Booking not found with ID: " + id));

        TestingServiceBookingStatus bookingStatus = serviceBooking.getStatus();

        if(bookingStatus == TestingServiceBookingStatus.CANCELLED
                || bookingStatus == TestingServiceBookingStatus.COMPLETED ){

            throw new AppException(400, "Cannot complete an already cancelled or " +
                    "completed Service Booking");
        }

        if(bookingStatus == TestingServiceBookingStatus.CONFIRMED
                || bookingStatus == TestingServiceBookingStatus.PENDING ){

            throw new AppException(400, "Cannot complete a Service Booking " +
                    "that has not started yet");
        }

        TimeFunctions.validateRealStartAndEndTime
                (serviceBooking.getExpectedStartTime(), serviceBooking.getExpectedEndTime(),
                        payload.getRealStartTime(), payload.getRealEndTime());

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(payload.getResultList());

        testingServiceBookingRepo.completeTestingServiceBooking
                (id, payload, result , TestingServiceBookingStatus.COMPLETED);
    }

    @Transactional
    public void updateTestingServiceBookingCommentAndRating
            (int id, EvaluatePayload payload) {

        boolean serviceBookingExist = testingServiceBookingRepo.existsById(id);

        if(serviceBookingExist){

            throw new AppException(404,
                    "Testing Service not found with ID " + id);
        }

        testingServiceBookingRepo.updateServiceBookingCommentAndRatingById(id, payload);
    }

    // Delete
    @Transactional
    public void cancelTestingServiceBooking(int id) {
        TestingServiceBooking
                serviceBooking = testingServiceBookingRepo
                .getTestingServiceBookingStatusById(id)
                .orElseThrow(() -> new AppException
                        (404, "Testing Service Booking not found with ID: " + id));

        TestingServiceBookingStatus bookingStatus = serviceBooking.getStatus();

        if(bookingStatus == TestingServiceBookingStatus.CANCELLED
        || bookingStatus == TestingServiceBookingStatus.COMPLETED ){

            throw new AppException(400, "Cannot cancel an already cancelled or " +
                    "completed Service Booking");
        }

        testingServiceBookingRepo.cancelTestingServiceBooking
                (id, TestingServiceBookingStatus.CANCELLED);
    }

    // Delete
    @Transactional
    public void deleteTestingServiceBooking(int id) {
        boolean serviceBookingExist = testingServiceBookingRepo.existsById(id);
        if (!serviceBookingExist) {
            throw new AppException(404, "Testing Service History not found with ID: " + id);
        }

        testingServicePaymentRepo.deleteServicePaymentById(id);
        testingServiceBookingRepo.deleteTestingServiceBooking(id);
    }
}
