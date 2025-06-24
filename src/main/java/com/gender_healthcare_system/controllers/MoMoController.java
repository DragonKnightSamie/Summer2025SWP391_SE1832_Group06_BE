package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.payloads.todo.MomoPaymentPayload;
import com.gender_healthcare_system.services.MomoPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/momo/payment-transaction")
@RequiredArgsConstructor
public class MoMoController {

    private final MomoPaymentService momoPaymentService;

    //create payment request
    @PostMapping("/create-payment-request")
    public ResponseEntity<?> createMomoPaymentRequest(@RequestBody MomoPaymentPayload payload) {
        try {
            String response = momoPaymentService.createMomoPaymentRequest(payload);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating payment request: " + e.getMessage());
        }
    }

   //callback Momo trả về
    @GetMapping("/callback")
    public ResponseEntity<?> handleMomoCallback(HttpServletRequest request) {
        momoPaymentService.getMomoPaymentTransactionInfo(request);
        return ResponseEntity.ok("Callback received");
    }

    //redirect URL for error handling
    @GetMapping("/check-error")
    public ResponseEntity<String> handlePaymentError() {
        return ResponseEntity.badRequest().body("Payment failed or canceled by user.");
    }
}
