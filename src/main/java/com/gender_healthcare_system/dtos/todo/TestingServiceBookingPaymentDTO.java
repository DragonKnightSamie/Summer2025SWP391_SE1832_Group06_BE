package com.gender_healthcare_system.dtos.todo;

import com.gender_healthcare_system.entities.enu.PaymentMethod;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestingServiceBookingPaymentDTO {

    private long amount;
    private PaymentMethod method;
    private PaymentStatus status;

}
