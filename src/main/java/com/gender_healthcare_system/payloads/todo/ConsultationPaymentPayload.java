package com.gender_healthcare_system.payloads.todo;

import com.gender_healthcare_system.entities.enu.PaymentMethod;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
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
public class ConsultationPaymentPayload implements Serializable {

    @NotBlank(message = "Transaction ID is required")
    @Length(min = 13, max = 20, message = "Order ID must be between 13 and 20 characters")
    private String transactionId;

    @NotNull(message = "Amount is required")
    private long amount;

    @NotNull(message = "Method is required")
    private PaymentMethod method;

    @Nationalized
    @Size(min = 5, max = 100, message = "Description must be either " +
            "empty or between 5 to 100 characters")
    private String description;
}
