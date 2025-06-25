package com.gender_healthcare_system.payloads.todo;

import com.gender_healthcare_system.entities.enu.PaymentMethod;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Service Id is required")
    private int serviceId;

    @NotNull(message = "Customer Id is required")
    private int customerId;

    @NotBlank(message = "Payment Transaction Id is required")
    @Length(min = 13, max = 20, message = "Order ID must be between 13 and 20 characters")
    private String paymentTransactionId;

    @NotNull(message = "Payment amount is required")
    private long paymentAmount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Nationalized
    @Size(min = 5, max = 100, message = "Description must be either " +
            "empty or between 5 to 100 characters")
    private String description;
}
