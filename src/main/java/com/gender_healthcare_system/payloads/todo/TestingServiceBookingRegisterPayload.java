package com.gender_healthcare_system.payloads.todo;

import com.gender_healthcare_system.entities.enu.PaymentMethod;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceBookingRegisterPayload implements Serializable {

    @NotNull
    private Integer serviceId;

    @NotNull
    private int customerId;

    @NotBlank
    @Length(min = 13, max = 20, message = "Order ID must be between 13 and 20 characters")
    private String paymentOrderId;

    @NotNull
    @Min(value = 10000, message = "Value must be at least 10,000 VND")
    @Max(value = 20000000, message = "Value must not exceed 20,000,000 VND")
    private Long paymentAmount;

    @NotNull
    private PaymentMethod paymentMethod;

    @Nationalized
    @Nullable
    @Size(min = 5, max = 100, message = "Description must be either " +
            "empty or between 5 to 100 characters")
    private String description;
}
