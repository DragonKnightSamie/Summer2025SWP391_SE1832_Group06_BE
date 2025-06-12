package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.TestingServiceDTO;
import com.gender_healthcare_system.dtos.TestingServiceListDTO;
import com.gender_healthcare_system.entities.enu.TestingServiceStatus;
import com.gender_healthcare_system.entities.todo.PriceList;
import com.gender_healthcare_system.entities.todo.TestingService;
import com.gender_healthcare_system.entities.todo.TestingServiceForm;
import com.gender_healthcare_system.entities.todo.TestingServiceType;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.PriceListPayload;
import com.gender_healthcare_system.payloads.TestingServiceRegisterPayload;
import com.gender_healthcare_system.payloads.TestingServiceUpdatePayload;
import com.gender_healthcare_system.repositories.TestingServiceRepo;
import com.gender_healthcare_system.repositories.TestingServiceTypeRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TestingService_Service {

    private final TestingServiceRepo testingServiceRepo;

    private final TestingServiceTypeRepo testingServiceTypeRepo;

    public TestingServiceDTO getTestingServiceById(int id) {
        return testingServiceRepo.getTestingServiceById(id)
                .orElseThrow(() -> new AppException
                        (404,"Testing Service not found with ID: " + id));
    }

    public Map<String, Object> getAllTestingServices
            (int page, String sortField, String sortOrder) {

        final int itemSize = 10;

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);

        if(sortOrder.equals("desc")){
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }

        Pageable pageRequest = PageRequest
                .of(page, itemSize, sort);


        Page<TestingServiceListDTO> pageResult =
                testingServiceRepo.getAllTestingServices(pageRequest);

        if(!pageResult.hasContent()){

            throw new AppException(404, "No Testing Services found");
        }

        List<TestingServiceListDTO> serviceList = pageResult.getContent();

        Map<String, Object> map = new HashMap<>();

        map.put("totalItems", pageResult.getTotalElements());
        map.put("testingServices", serviceList);
        map.put("totalPages", pageResult.getTotalPages());
        map.put("currentPage", pageResult.getNumber());

        return map;
    }

    // Create a new testing service
    public void createTestingService
    (TestingServiceRegisterPayload payload) {

        TestingService newService = new TestingService();
        TestingServiceForm newForm = new TestingServiceForm();

        TestingServiceType service = testingServiceTypeRepo
                .findById(payload.getServiceTypeId())
                .orElseThrow(() -> new AppException
                        (404, "No service type found with ID " + payload.getServiceTypeId()));

        newService.setTestingServiceType(service);
        newService.setServiceName(payload.getServiceName());
        newService.setDescription(payload.getDescription());
        newService.setStatus(TestingServiceStatus.AVAILABLE);

        newForm.setContent(payload.getServiceFormContent());
        newService.setTestingServiceForm(newForm);


        for(PriceListPayload item: payload.getPriceList()) {
            PriceList priceItem = new PriceList();

            priceItem.setDescription(item.getDescription());
            priceItem.setPrice(item.getPrice());

            newService.addPriceItem(priceItem);

        }

        /*if (testingService == null) {
            throw new IllegalArgumentException("Testing Service cannot be null");
        }*/

         testingServiceRepo.saveAndFlush(newService);
    }

    @Transactional
    public void updateTestingService(int id, TestingServiceUpdatePayload payload) {
        boolean serviceExists = testingServiceRepo.existsById(id);
        if (!serviceExists) {
            throw new AppException(404, "Testing Service not found with ID: " + id);
        }
        testingServiceRepo.updateTestingService(id, payload);

    }


    public void deleteTestingService(int id) {
        boolean serviceExists = testingServiceRepo.existsById(id);
        if (!serviceExists) {
            throw new AppException(404, "Testing Service not found with ID: " + id);
        }
        testingServiceRepo.deleteById(id);
    }


}
