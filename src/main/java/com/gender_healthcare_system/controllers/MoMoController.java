package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.payloads.todo.MomoPaymentPayload;
import com.gender_healthcare_system.services.MomoPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/momo/payment-transactions")
@RequiredArgsConstructor
public class MoMoController {

    private final MomoPaymentService momoPaymentService;

    //create payment request
    @PostMapping("/create-payment-request")
    public ResponseEntity<?> createMomoPaymentRequest
    (@RequestBody @Valid MomoPaymentPayload payload) {
        try {
            String response = momoPaymentService.createMomoPaymentRequest(payload);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating payment request: " + e.getMessage());
        }
    }

    /*//create refund payment for testing service booking
    @PostMapping("/payment-transaction/create-refund")
    //@PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<String> createPaymentRefundRequest
    (@RequestBody MomoPaymentRefundPayload payload) throws Exception {

        return ResponseEntity.ok(momoPaymentService.createMomoRefundPaymentRequest(payload));
    }*/

    //redirect URL for error handling
    @GetMapping("/check-error")
    public ResponseEntity<String> handlePaymentError(
        @RequestParam String transactionId,
        @RequestParam String resultCode) {

            if ("0".equals(resultCode)) {
                // payment success
                return ResponseEntity.ok(
                        "Payment successful for transactionId " + transactionId);
            } else {
                // payment failed
                return ResponseEntity.status(400).body("Payment failed with code: " + resultCode);
            }
        }
}
