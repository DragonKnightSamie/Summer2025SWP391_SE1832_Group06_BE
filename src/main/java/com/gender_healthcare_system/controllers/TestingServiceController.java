package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.TestingServiceDTO;
import com.gender_healthcare_system.entities.todo.TestingService;
import com.gender_healthcare_system.payloads.PriceListUpdatePayload;
import com.gender_healthcare_system.payloads.TestingServiceFormUpdatePayload;
import com.gender_healthcare_system.payloads.TestingServiceUpdatePayload;
import com.gender_healthcare_system.services.PriceListService;
import com.gender_healthcare_system.services.TestingServiceFormService;
import com.gender_healthcare_system.services.TestingService_Service;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class TestingServiceController {

    private final TestingServiceFormService testingServiceFormService;
    private final TestingService_Service testingService_Service;
    private final PriceListService priceListService;

    //get testing service by ID
    @GetMapping("/testing-service/{id}")
    public TestingServiceDTO getTestingServiceById(@PathVariable int id) {
        return testingService_Service.getTestingServiceById(id);
    }

    //get all testing services
    public List<TestingService> getAllTestingServices() {
        return testingService_Service.getAllTestingServices();
    }

    //create new testing service
    @PostMapping("/testing-service/create")
    public TestingService createTestingService(@RequestBody TestingService testingService) {
        return testingService_Service.createTestingService(testingService);
    }

    //update testing service
    @PutMapping("/testing-service/{id}")
    public void updateTestingService(@PathVariable int id, @RequestBody TestingServiceUpdatePayload payload) {
        testingService_Service.updateTestingService(id, payload);
    }

    //delete testing service
    @DeleteMapping("/testing-service/{id}")
    public void deleteTestingService(@PathVariable int id) {
        testingService_Service.deleteTestingService(id);
    }

    /// //////////////////////////// Testing Service Form Operations ///////////////////////////////
    //update testing service form by ID
    @PutMapping("/testing-service-form/{id}")
    public void updateTestingServiceFormById(@PathVariable int id, @RequestBody TestingServiceFormUpdatePayload payload) {
        testingServiceFormService.updateTestingServiceFormById(id, payload);
    }

    //delete testing service form by ID
    @DeleteMapping("/testing-service-form/{id}")
    public void deleteTestingServiceFormById(@PathVariable int id) {
        testingServiceFormService.deleteTestingServiceFormById(id);
    }

    /// //////////////////////////// Price List Operations ///////////////////////////////
    //update price list by ID
    @PutMapping("/price-list/{id}")
    public void updatePriceList(@PathVariable int id, @RequestBody PriceListUpdatePayload payload) {
        priceListService.updatePriceList(id, payload);
    }

    //delete price list by ID
    @DeleteMapping("/price-list/{id}")
    public void deletePriceList(@PathVariable int id) {
        priceListService.deletePriceList(id);
    }
}
