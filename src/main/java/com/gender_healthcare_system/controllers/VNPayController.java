package com.gender_healthcare_system.controllers;


import com.gender_healthcare_system.payloads.todo.VNPayRefundPayload;
import com.gender_healthcare_system.services.VNPayService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/vnpay/payment-transaction")
class VNPayController {

    private final VNPayService vnPayService;

    @PostMapping("/create-payment-url")
    public ResponseEntity<String> createVNPayPaymentUrl(
            @RequestParam
            @Min(value = 10000, message = "Amount must be at least 10 000 VND")
            @Max(value = 20000000, message = "Amount cannot exceed 20 000 000 VND")
            Long amount,
            @RequestParam(defaultValue = "http://localhost:8080/api/v1/vnpay/" +
                    "payment-transaction/check-payment-error")
            @Length(min = 5, max = 255, message = "Redirect URL " +
                    "must be between 5 and 255 characters")
            String redirectUrl,
            HttpServletRequest request){

        return ResponseEntity.ok
                (vnPayService.createPaymentUrl(amount, redirectUrl, request));
    }

    /*@PostMapping("/create-transaction-refund-request")
    public ResponseEntity<String> createVNPayTransactionRefundRequest(
            @RequestBody @Valid VNPayRefundPayload payload,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        return ResponseEntity.ok(
                vnPayService.createTransactionRefundRequest(payload, request, response));
    }*/

    @GetMapping("/check-payment-error")
    public ResponseEntity<String> checkPaymentErrorCode(
            @RequestParam String transactionNo,
            @RequestParam String responseCode,
            @RequestParam String transactionStatus){
            //HttpServletRequest request){

        return ResponseEntity.ok(
                vnPayService.checkPaymentError(transactionNo, responseCode, transactionStatus));
    }
}
