package com.gender_healthcare_system.dtos.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceListDTO {

    private int priceId;
    private long price;
    private String description;
}
