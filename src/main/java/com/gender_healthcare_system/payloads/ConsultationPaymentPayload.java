package com.gender_healthcare_system.payloads;

import com.gender_healthcare_system.entities.enu.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationPaymentPayload {

    @NotNull
    private double amount;

    @NotNull
    private PaymentMethod method;
}
