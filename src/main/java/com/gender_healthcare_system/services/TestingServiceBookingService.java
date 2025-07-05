package com.gender_healthcare_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gender_healthcare_system.dtos.todo.TestingServiceBookingDTO;
import com.gender_healthcare_system.dtos.todo.TestingServiceResultDTO;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import com.gender_healthcare_system.entities.enu.Rating;
import com.gender_healthcare_system.entities.enu.TestingServiceBookingStatus;
import com.gender_healthcare_system.entities.todo.TestingService;
import com.gender_healthcare_system.entities.todo.TestingServiceBooking;
import com.gender_healthcare_system.entities.todo.TestingServicePayment;
import com.gender_healthcare_system.entities.user.Account;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.todo.*;
import com.gender_healthcare_system.repositories.*;
import com.gender_healthcare_system.utils.UtilFunctions;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class TestingServiceBookingService {

    private final AccountRepo accountRepo;
    private final TestingServiceRepo testingServiceRepo;
    private final TestingServiceBookingRepo testingServiceBookingRepo;
    private final TestingServicePaymentRepo testingServicePaymentRepo;

    // Get TestingServiceBooking entity by id
    public TestingServiceBookingDTO getTestingServiceBookingDetailsById(int id)
            throws JsonProcessingException {
        TestingServiceBookingDTO testingService = testingServiceBookingRepo
                .getTestingBookingDetailsById(id)
                .orElseThrow(() -> new AppException
                        (404, "Testing Service Booking not found with ID: " + id));

        String result = testingService.getResult();

        if(!StringUtils.isEmpty(result)){
            ObjectMapper mapper = new ObjectMapper();

            List<TestingServiceResultDTO> resultList = mapper.readValue(result, new
                    TypeReference<>(){});

            testingService.setResults(resultList);
        }

        return testingService;
    }

    // Get all TestingServiceBookings by Customer ID with pagination
    public Map<String, Object> getAllTestingServiceBookingsByCustomerId
    (int customerId, int page, String sortField, String sortOrder) {

        boolean customerExist = accountRepo.existsById(customerId);
        if (!customerExist) {
            throw new AppException(404, "Customer not found with ID: " + customerId);
        }

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);
        if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageable = PageRequest.of(page, itemSize, sort);
        Page<TestingServiceBookingDTO> pageResult =
                testingServiceBookingRepo
                        .getAllTestingServiceBookingsByCustomerId(customerId, pageable);

        if (!pageResult.hasContent()) {
            throw new AppException(404, "No Testing Service Bookings found");
        }

        List<TestingServiceBookingDTO> bookingList = pageResult.getContent();
        bookingList.forEach(x -> x.setCustomerName(null));

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", pageResult.getTotalElements());
        response.put("testingServiceBookings", bookingList);
        response.put("totalPages", pageResult.getTotalPages());
        response.put("currentPage", pageResult.getNumber());

        return response;
    }

    // Get all TestingServiceBookings by Staff ID with pagination
    public Map<String, Object> getAllTestingServiceBookingsByStaffId
            (int staffId, int page, String sortField, String sortOrder) {

        boolean staffExist = accountRepo.existsById(staffId);
        if (!staffExist) {
            throw new AppException(404, "Staff not found with ID: " + staffId);
        }

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);
        if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageable = PageRequest.of(page, itemSize, sort);
        Page<TestingServiceBookingDTO> pageResult =
                testingServiceBookingRepo
                        .getAllTestingServiceBookingsByStaffId
                                (staffId, pageable);

        if (!pageResult.hasContent()) {
            throw new AppException(404, "No Testing Service Bookings found");
        }

        List<TestingServiceBookingDTO> bookingList = pageResult.getContent();
        bookingList.forEach(x -> x.setStaffName(null));

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", pageResult.getTotalElements());
        response.put("testingServiceBookings", bookingList);
        response.put("totalPages", pageResult.getTotalPages());
        response.put("currentPage", pageResult.getNumber());

        return response;
    }

    public List<LocalDateTime> getBookingScheduleForADay
            (TestingServiceBookingSchedulePayload payload){

        List<LocalDateTime> bookingSchedule =
                testingServiceBookingRepo.getBookingScheduleInADate
                        (payload.getCheckDate(), TestingServiceBookingStatus.CANCELLED);

        List<LocalDateTime> customerBookedSchedule =
                testingServiceBookingRepo
                        .getCustomerBookedScheduleInADate
                                (payload.getServiceId(), payload.getCustomerId(),
                                        payload.getCheckDate(),
                                        TestingServiceBookingStatus.CANCELLED);

        return Stream.concat(bookingSchedule.stream(), customerBookedSchedule.stream())
                .distinct()
                .sorted()
                .toList();
    }

    // Create new TestingServiceBooking with payment
    @Transactional
    public void createTestingServiceBooking
            (TestingServiceBookingRegisterPayload payload) {

        boolean bookingExist = testingServiceBookingRepo
                .existsByTestingService_ServiceIdAndCustomer_AccountIdAndExpectedStartTime
                        (payload.getServiceId(), payload.getCustomerId(), payload.getExpectedStartTime());

        if (bookingExist) {
            throw new AppException(409, "Testing Service has already been booked");
        }

        Account customer = accountRepo.findById(payload.getCustomerId())
                .orElseThrow(() -> new AppException(404,
                        "Customer not found with ID " + payload.getCustomerId()));

        TestingService testingService = testingServiceRepo.findById(payload.getServiceId())
                .orElseThrow(() -> new AppException(404,
                        "Testing Service not found with ID " + payload.getServiceId()));

        // Auto-assign staff based on availability
        LocalDate dateExtraction = payload.getExpectedStartTime().toLocalDate();
        List<Object[]> staffList = accountRepo.findStaffOrderedByLeastTests(dateExtraction);
        
        if(staffList.isEmpty()) {
            throw new AppException(500, "No Staff found to assign Test to, please try again later");
        }

        Object[] firstStaff = staffList.getFirst();
        Account staff = accountRepo.findById((Integer) firstStaff[1])
                .orElseThrow(() -> new AppException(500, "Error when trying to get Staff info"));

        TestingServiceBooking testingServiceBooking = new TestingServiceBooking();

        testingServiceBooking.setCreatedAt(UtilFunctions.getCurrentDateTimeWithTimeZone());
        testingServiceBooking.setExpectedStartTime(payload.getExpectedStartTime());
        LocalDateTime expectedEndTime = payload.getExpectedStartTime().plusHours(1);
        testingServiceBooking.setExpectedEndTime(expectedEndTime);
        testingServiceBooking.setStatus(TestingServiceBookingStatus.CONFIRMED);
        testingServiceBooking.setRating(Rating.AVERAGE);
        testingServiceBooking.setCustomer(customer);
        testingServiceBooking.setStaff(staff);
        testingServiceBooking.setTestingService(testingService);

        testingServiceBookingRepo.saveAndFlush(testingServiceBooking);

        TestingServicePayment testingServicePayment = new TestingServicePayment();

        testingServicePayment.setTestingServiceBooking(testingServiceBooking);
        testingServicePayment.setTransactionId(payload.getPayment().getTransactionId());
        testingServicePayment.setAmount(payload.getPayment().getAmount());
        testingServicePayment.setMethod(payload.getPayment().getMethod());
        testingServicePayment.setCreatedAt(UtilFunctions.getCurrentDateTimeWithTimeZone());
        testingServicePayment.setDescription(payload.getPayment().getDescription());
        testingServicePayment.setStatus(PaymentStatus.PAID);

        testingServicePaymentRepo.saveAndFlush(testingServicePayment);
    }

    @Transactional
    public void completeTestingServiceBooking
    (int id, TestingServiceBookingCompletePayload payload) throws JsonProcessingException {
        boolean bookingExist = testingServiceBookingRepo.existsById(id);

        if (!bookingExist) {
            throw new AppException(404, "Testing Service Booking not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(payload.getResultList());

        testingServiceBookingRepo.completeTestingServiceBooking
                (id, payload.getRealStartTime(), payload.getRealEndTime(), result,
                        TestingServiceBookingStatus.COMPLETED);
    }

    @Transactional
    public void startTestingServiceBooking(int id, LocalDateTime realStartTime) {
        boolean bookingExist = testingServiceBookingRepo.existsById(id);

        if (!bookingExist) {
            throw new AppException(404, "Testing Service Booking not found");
        }

        testingServiceBookingRepo.startTestingServiceBooking(id, realStartTime);
    }

    @Transactional
    public void updateTestingServiceBookingCommentAndRating
            (int id, EvaluatePayload payload) {
        boolean bookingExist = testingServiceBookingRepo.existsById(id);

        if (!bookingExist) {
            throw new AppException(404, "Testing Service Booking not found");
        }

        testingServiceBookingRepo.updateServiceBookingCommentAndRatingById(id, payload);
    }

    @Transactional
    public void cancelTestingServiceBooking(int id) {
        boolean bookingExist = testingServiceBookingRepo.existsById(id);

        if (!bookingExist) {
            throw new AppException(404, "Testing Service Booking not found");
        }

        testingServiceBookingRepo.cancelTestingServiceBooking(id, TestingServiceBookingStatus.CANCELLED);
    }

    @Transactional
    public void deleteTestingServiceBooking(int id) {
        boolean bookingExist = testingServiceBookingRepo.existsById(id);

        if (!bookingExist) {
            throw new AppException(404, "Testing Service Booking not found");
        }

        testingServiceBookingRepo.deleteTestingServiceBooking(id);
    }
}
