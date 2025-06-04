package com.gender_healthcare_system.services;

import com.gender_healthcare_system.entities.enu.PaymentStatus;
import com.gender_healthcare_system.entities.todo.Payment;
import com.gender_healthcare_system.repositories.PaymentRepo;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {

    private final PaymentRepo paymentRepo;

    //getALlPayments
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    //getPaymentById
    public Payment getPaymentById(int id) {
        return paymentRepo.findById(id).orElse(null);
    }

    //createPayment
    public Payment createPayment(Payment payment) {
        return paymentRepo.save(payment);
    }

    //updatePayment
    public Payment updatePayment(int id, Payment payment) {
        if (paymentRepo.existsById(id)) {
            payment.setPaymentId(id);
            return paymentRepo.save(payment);
        }
        return null;
    }

    //deletePayment
    public void deletePayment(int id) {
        if (paymentRepo.existsById(id)) {
            paymentRepo.deleteById(id);
        }
    }

    //getPaymentByStaffId
    /*public List<Payment> getPaymentsByStaffId(int staffId) {
        return paymentRepo.findByStaff_StaffId(staffId);
    }*/

    //getPaymentsByStatus
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepo.findByStatus(status);
    }

    //updatePaymentStatus
    public Payment updatePaymentStatus(int id, PaymentStatus newStatus) {
        Payment payment = paymentRepo.findById(id).orElse(null);
        if (payment != null) {
            payment.setStatus(newStatus);
            return paymentRepo.save(payment);
        }
        return null;
    }

}