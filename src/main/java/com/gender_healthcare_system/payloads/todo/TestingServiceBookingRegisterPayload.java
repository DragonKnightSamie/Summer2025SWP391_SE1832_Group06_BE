package com.gender_healthcare_system.payloads.todo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceBookingRegisterPayload implements Serializable {

    @NotNull(message = "Service ID is required")
    private Integer serviceId;

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @Valid
    @NotNull(message = "Testing Booking Payment is required")
    private PaymentPayload payment;
}
