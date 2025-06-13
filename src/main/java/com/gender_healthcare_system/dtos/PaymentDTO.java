package com.gender_healthcare_system.dtos;

import com.gender_healthcare_system.entities.enu.PaymentMethod;
import com.gender_healthcare_system.entities.enu.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentDTO {

    private int paymentId;
    private double amount;
    private PaymentMethod method;
    private PaymentStatus status;

    private TestingServiceHistoryDTO testingServiceHistory;

    //CONSTRUCTOR cho testingServiceHistoryDTO
    public PaymentDTO(int paymentId, double amount, PaymentMethod method, PaymentStatus status) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.method = method;
        this.status = status;
    }

    public PaymentDTO(int paymentId, double amount, PaymentMethod method, PaymentStatus status, TestingServiceHistoryDTO testingServiceHistory) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.testingServiceHistory = testingServiceHistory;
    }
}
