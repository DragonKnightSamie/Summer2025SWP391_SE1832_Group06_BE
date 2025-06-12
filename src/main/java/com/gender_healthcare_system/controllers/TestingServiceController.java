package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.dtos.TestingServiceDTO;
import com.gender_healthcare_system.dtos.TestingServiceListDTO;
import com.gender_healthcare_system.services.PriceListService;
import com.gender_healthcare_system.services.TestingServiceFormService;
import com.gender_healthcare_system.services.TestingService_Service;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@AllArgsConstructor
//@RestController
//@RequestMapping("/api")
public class TestingServiceController {

/*    private final TestingServiceFormService testingServiceFormService;
    private final TestingService_Service testingService_Service;
    private final PriceListService priceListService;

    //get testing service by ID
    @GetMapping("/testing-services/{id}")
    public TestingServiceDTO getTestingServiceById(@PathVariable int id) {
        return testingService_Service.getTestingServiceById(id);
    }

    //get all testing services
    @GetMapping("/testing-services/")
    public List<TestingServiceListDTO> getAllTestingServices() {
        return testingService_Service.getAllTestingServices();
    }

    //create new testing service
    @PostMapping("/testing-services/create")
    public String createTestingService
    (@RequestBody payload) {
            testingService_Service.createTestingService(payload);
        return "Testing Service created successfully";
    }

    //update testing service
    @PutMapping("/testing-services/update/{id}")
    public String updateTestingService
    (@PathVariable int id, @RequestBody  payload) {

        testingService_Service.updateTestingService(id, payload);
        return "Testing Service updated successfully";
    }

    //delete testing service
    @DeleteMapping("/testing-services/{id}")
    public String deleteTestingService(@PathVariable int id) {

        testingService_Service.deleteTestingService(id);
        return "Testing Service deleted successfully";
    }

    /// //////////////////////////// Testing Service Form Operations ///////////////////////////////
    //update testing service form by ID
    @PutMapping("/testing-service-forms/{id}")
    public void updateTestingServiceFormById
    (@PathVariable int id, @RequestBody @NotBlank
     @Length(min = 5, max = 255, message = "Content must be between 5 and 255 characters")
     String newContent) {
        testingServiceFormService.updateTestingServiceFormById(id, newContent);
    }

    //delete testing service form by ID
    @DeleteMapping("/testing-service-forms/{id}")
    public void deleteTestingServiceFormById(@PathVariable int id) {
        testingServiceFormService.deleteTestingServiceFormById(id);
    }

    /// //////////////////////////// Price List Operations ///////////////////////////////
    //update price list by ID
    @PutMapping("/price-lists/{id}")
    public void updatePriceList(@PathVariable int id,
                                @RequestBody  payload) {
        priceListService.updatePriceList(id, payload);
    }

    //delete price list by ID
    @DeleteMapping("/price-lists/{id}")
    public void deletePriceList(@PathVariable int id) {
        priceListService.deletePriceList(id);
    }
    */
}
