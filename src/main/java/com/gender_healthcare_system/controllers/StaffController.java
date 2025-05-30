package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.entities.enu.PaymentStatus;
import com.gender_healthcare_system.entities.todo.Payment;
import com.gender_healthcare_system.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff/payments")
public class StaffController {

    @Autowired
    private PaymentService paymentService;

    // 1. Get all payments
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // 2. Get payment by ID
    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable int id) {
        return paymentService.getPaymentById(id);
    }

    // 3. Create new payment
    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    // 4. Update payment
    @PutMapping("/{id}")
    public Payment updatePayment(@PathVariable int id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment);
    }

    // 5. Delete payment
    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable int id) {
        paymentService.deletePayment(id);
    }

    // 6. Get payments by staff ID
    @GetMapping("/by-staff/{staffId}")
    public List<Payment> getPaymentsByStaffId(@PathVariable int staffId) {
        return paymentService.getPaymentsByStaffId(staffId);
    }

    // 7. Get payments by status
    @GetMapping("/by-status/{status}")
    public List<Payment> getPaymentsByStatus(@PathVariable String status) {
        try {
            PaymentStatus parsedStatus = PaymentStatus.valueOf(status.toUpperCase());
            return paymentService.getPaymentsByStatus(parsedStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + status);
        }
    }

    // 8. Update payment status
    @PutMapping("/{id}/update-status")
    public Payment updatePaymentStatus(@PathVariable int id, @RequestParam String newStatus) {
        try {
            PaymentStatus status = PaymentStatus.valueOf(newStatus.toUpperCase());
            return paymentService.updatePaymentStatus(id, status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status: " + newStatus);
        }
    }
}
