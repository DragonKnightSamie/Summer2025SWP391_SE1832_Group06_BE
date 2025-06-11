package com.gender_healthcare_system.services;

import com.gender_healthcare_system.payloads.PriceListUpdatePayload;
import com.gender_healthcare_system.repositories.PriceListRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PriceListService {

    private final PriceListRepo priceListRepo;

    //updatePriceList
    public void updatePriceList(int id, PriceListUpdatePayload payload) {
        boolean priceListExists = priceListRepo.existsById(id);
        if (!priceListExists) {
            throw new RuntimeException("Price List not found with ID: " + id);
        }
        priceListRepo.updatePriceListById(id, payload);
    }

    //deletePriceList
    public void deletePriceList(int id) {
        boolean priceListExists = priceListRepo.existsById(id);
        if (!priceListExists) {
            throw new RuntimeException("Price List not found with ID: " + id);
        }
        priceListRepo.deleteByPriceId(id);
    }
}
