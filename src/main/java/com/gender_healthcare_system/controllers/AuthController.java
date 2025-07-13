package com.gender_healthcare_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gender_healthcare_system.services.EmailService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Các API xác thực và quên mật khẩu")
@AllArgsConstructor
public class AuthController {
    private final EmailService emailService;

    @Operation(summary = "Quên mật khẩu", description = "Gửi email đặt lại mật khẩu cho người dùng thông qua Firebase Authentication")
    @PostMapping("/forgot-password/")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            System.out.println("Processing forgot password request for email: " + email);

            // Kiểm tra Firebase đã được khởi tạo chưa
            if (FirebaseApp.getApps().isEmpty()) {
                System.err.println("Firebase not initialized!");
                return ResponseEntity.status(500).body("Hệ thống đang bảo trì. Vui lòng thử lại sau.");
            }

            System.out.println("Firebase apps count: " + FirebaseApp.getApps().size());

            // Kiểm tra email có hợp lệ không
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email không được để trống");
            }

            // Kiểm tra format email
            if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
                return ResponseEntity.badRequest().body("Email phải là địa chỉ Gmail hợp lệ");
            }

            String link = FirebaseAuth.getInstance().generatePasswordResetLink(email);
            System.out.println("Generated reset link successfully");

            emailService.sendResetPasswordEmail(email, link);
            System.out.println("Email sent successfully");

            return ResponseEntity.ok("Đã gửi email đặt lại mật khẩu! Vui lòng kiểm tra hộp thư.");
        } catch (com.google.firebase.auth.FirebaseAuthException e) {
            System.err.println("Firebase Auth Error: " + e.getMessage());
            if (e.getMessage().contains("USER_NOT_FOUND")) {
                return ResponseEntity.badRequest().body("Email không tồn tại trong hệ thống");
            }
            return ResponseEntity.badRequest().body("Lỗi xác thực: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error in forgotPassword: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Có lỗi xảy ra. Vui lòng thử lại sau.");
        }
    }

    @Operation(summary = "Kiểm tra trạng thái Firebase", description = "Kiểm tra xem Firebase đã được khởi tạo chưa")
    @PostMapping("/test-firebase/")
    public ResponseEntity<?> testFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                return ResponseEntity.status(500).body("Firebase not initialized");
            }
            int appCount = FirebaseApp.getApps().size();
            String appName = FirebaseApp.getApps().get(0).getName();
            return ResponseEntity.ok("Firebase status: " + appCount + " app(s) initialized. Current app: " + appName);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Firebase error: " + e.getMessage());
        }
    }
}
