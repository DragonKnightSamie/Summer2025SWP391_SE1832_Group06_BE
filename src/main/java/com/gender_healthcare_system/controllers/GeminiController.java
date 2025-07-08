package com.gender_healthcare_system.controllers;

import com.gender_healthcare_system.services.GeminiService;
import com.gender_healthcare_system.dtos.todo.MessageRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    /**
     * Send a message to Gemini with a persistent user session (multi-turn chat).
     *
     * @param userId  the ID of the user
     * @param request the message sent from user
     * @return Gemini's reply as String
     */
    @PostMapping("/{userId}")
    public ResponseEntity<String> chat(@PathVariable String userId, @RequestBody MessageRequestDTO request) {
        String response = geminiService.sendMessage(userId, request.getMessage());
        return ResponseEntity.ok(response);
    }

    /**
     * Clear a user's chat session.
     *
     * @param userId the ID of the user
     * @return success message
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearSession(@PathVariable String userId) {
        geminiService.clearSession(userId);
        return ResponseEntity.ok("Session cleared for user: " + userId);
    }

    /**
     * Get a user's chat history.
     *
     * @param userId the ID of the user
     * @return the chat history
     */
    @GetMapping("/{userId}/history")
    public ResponseEntity<?> getHistory(@PathVariable String userId) {
        return ResponseEntity.ok(geminiService.getSessionHistory(userId));
    }

    /**
     * Message payload for chat request
     */
    @Data
    @AllArgsConstructor
    public static class MessageRequest {
        private String message;
    }
}
