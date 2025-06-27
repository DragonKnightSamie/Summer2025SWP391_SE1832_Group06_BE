package com.gender_healthcare_system.payloads.todo;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceListRegisterPayload implements Serializable {

    @NotNull(message = "Price is required")
    @Min(value = 10000, message = "Value must be at least 10,000 VND")
    @Max(value = 20000000, message = "Value must not exceed 20,000,000 VND")
    private Long price;

    @Nationalized
    @Size(min = 5,max = 255, message = "Description must be empty or between 5 and 255 characters")
    private String description;
}
