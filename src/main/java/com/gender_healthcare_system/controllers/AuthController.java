package com.gender_healthcare_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gender_healthcare_system.services.EmailService;
import com.google.firebase.auth.FirebaseAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Các API xác thực và quên mật khẩu")
public class AuthController {
    @Autowired
    private EmailService emailService;

    @Operation(summary = "Quên mật khẩu", description = "Gửi email đặt lại mật khẩu cho người dùng thông qua Firebase Authentication")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            String link = FirebaseAuth.getInstance().generatePasswordResetLink(email);
            emailService.sendResetPasswordEmail(email, link);
            return ResponseEntity.ok("Đã gửi email đặt lại mật khẩu! Vui lòng kiểm tra hộp thư.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email không tồn tại hoặc lỗi: " + e.getMessage());
        }
    }
}
