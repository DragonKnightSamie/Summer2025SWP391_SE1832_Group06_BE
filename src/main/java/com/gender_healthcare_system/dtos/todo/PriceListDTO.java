package com.gender_healthcare_system.dtos.todo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gender_healthcare_system.entities.enu.PriceListStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceListDTO implements Serializable {

    private Integer priceId;

    private Long price;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PriceListStatus status;

    public PriceListDTO(Integer priceId, Long price, String description) {
        this.priceId = priceId;
        this.price = price;
        this.description = description;
    }
}
