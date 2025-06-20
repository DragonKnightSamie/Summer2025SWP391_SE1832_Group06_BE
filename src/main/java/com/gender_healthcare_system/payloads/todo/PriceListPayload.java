package com.gender_healthcare_system.payloads.todo;


import jakarta.annotation.Nullable;
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
public class PriceListPayload implements Serializable {

    @NotNull
    private long price;

    @Nullable
    @Nationalized
    @Size(min = 5,max = 255, message = "Description must be empty or between 5 and 255 characters")
    private String description;
}
