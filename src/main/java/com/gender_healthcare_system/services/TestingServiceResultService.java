package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.todo.TestingServiceResultDTO;
import com.gender_healthcare_system.entities.enu.Gender;
import com.gender_healthcare_system.entities.enu.GenderType;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.TestingServiceBookingRepo;
import com.gender_healthcare_system.repositories.TestingServiceRepo;
import com.gender_healthcare_system.repositories.TestingServiceResultRepo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TestingServiceResultService {

    private final TestingServiceResultRepo testingServiceResultRepo;

    private final TestingServiceBookingRepo testingServiceBookingRepo;

    private final TestingServiceRepo testingServiceRepo;

    private final AccountRepo accountRepo;

    public List<TestingServiceResultDTO>
            getAllServiceResultsByBookingId(int testingBookingId){

        boolean bookingExist = testingServiceBookingRepo.existsById(testingBookingId);

        if(!bookingExist){
            throw new AppException(404,
                    "No Testing Booking found with ID " +testingBookingId);
        }

        Gender customerGender =
                accountRepo.getCustomerGenderByBookingId(testingBookingId);

        GenderType genderType = GenderType.MALE;

        if(customerGender == Gender.MALE){

            genderType =GenderType.FEMALE;
        }

        return testingServiceResultRepo.
                getAllServiceResultsByTypeIdAndGenderType(testingBookingId, genderType);

    }

    public List<TestingServiceResultDTO>
    getAllServiceResultsByServiceId(int testingServiceId){

        boolean serviceExist = testingServiceRepo.existsById(testingServiceId);

        if(!serviceExist){
            throw new AppException(404,
                    "No Testing Service found with ID " +testingServiceId);
        }

        return testingServiceResultRepo.
                getAllServiceResultsByServiceId(testingServiceId);

    }
    /*@Transactional
    public void updateTestingServiceResult
            (int id, TestingServiceResultPayload payload) {
        boolean serviceResultExists = testingServiceResultRepo.existsById(id);
        if (!serviceResultExists) {
            throw new AppException(404, "Testing Service result not found with ID: " + id);
        }

        testingServiceResultRepo.updateTestingServiceResult(id, payload);

    }*/

    public void deleteTestingServiceResult(int id) {
        boolean serviceResultExists = testingServiceResultRepo.existsById(id);
        if (!serviceResultExists) {
            throw new AppException(404, "Testing Service result not found with ID: " + id);
        }
        testingServiceResultRepo.deleteTestingServiceResultById(id);
    }
}
