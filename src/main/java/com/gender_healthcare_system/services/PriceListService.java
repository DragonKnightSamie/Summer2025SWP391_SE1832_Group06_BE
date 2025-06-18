package com.gender_healthcare_system.services;

import com.gender_healthcare_system.dtos.todo.PriceListDTO;
import com.gender_healthcare_system.entities.todo.PriceList;
import com.gender_healthcare_system.entities.todo.TestingService;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.payloads.todo.PriceListPayload;
import com.gender_healthcare_system.repositories.PriceListRepo;
import com.gender_healthcare_system.repositories.TestingServiceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PriceListService {

    private final PriceListRepo priceListRepo;

    private final TestingServiceRepo testingServiceRepo;

    public List<PriceListDTO> getPriceListForTestingService(int serviceId){

        boolean testingService = testingServiceRepo
                .existsById(serviceId);

        if(!testingService){

            throw new AppException(404,
                            "No Testing Service found with ID " + serviceId);
        }

        List<PriceListDTO> priceList =
                priceListRepo.getPriceListForTestingService(serviceId);

        if(priceList.isEmpty()){

            throw new AppException(404,
                    "No Price List found for Testing Service with ID " + serviceId);
        }

        return priceList;
    }

    public void createNewPriceListForExistingService(int id, PriceListPayload payload) {

        TestingService testingService = testingServiceRepo
                .getTestingService(id)
                .orElseThrow(() ->
                        new AppException(404, "No Testing Service found with ID " + id));

        PriceList priceList = new PriceList();

        priceList.setTestingService(testingService);
        priceList.setPrice(payload.getPrice());
        priceList.setDescription(payload.getDescription());

        priceListRepo.saveAndFlush(priceList);
    }

    //updatePriceList
    @Transactional
    public void updatePriceList(int id, PriceListPayload payload) {
        boolean priceListExists = priceListRepo.existsById(id);
        if (!priceListExists) {
            throw new AppException(404, "Price List not found with ID: " + id);
        }
        priceListRepo.updatePriceListById(id, payload);
    }

    //deletePriceList
    @Transactional
    public void deletePriceList(int id) {
        boolean priceListExists = priceListRepo.existsById(id);
        if (!priceListExists) {
            throw new AppException(404, "Price List not found with ID: " + id);
        }
        priceListRepo.deleteByPriceId(id);
    }
}
