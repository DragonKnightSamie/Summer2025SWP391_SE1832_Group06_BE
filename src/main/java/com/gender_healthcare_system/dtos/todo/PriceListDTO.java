package com.gender_healthcare_system.dtos.todo;

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
}
