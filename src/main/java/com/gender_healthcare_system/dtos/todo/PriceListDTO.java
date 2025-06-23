package com.gender_healthcare_system.dtos.todo;

import com.gender_healthcare_system.entities.enu.PriceListStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceListDTO implements Serializable {

    private int priceId;
    private long price;
    private String description;
    private PriceListStatus status;

    public PriceListDTO(int priceId, long price, String description) {
        this.priceId = priceId;
        this.price = price;
        this.description = description;
    }
}
