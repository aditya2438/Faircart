package com.faircart.controller;

import com.faircart.dto.ApiResponse;
import com.faircart.dto.chat.ChatMessageRequest;
import com.faircart.dto.chat.ChatMessageResponse;
import com.faircart.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${faircart.api.base-path:/api/v1}/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            Authentication authentication, @Valid @RequestBody ChatMessageRequest request) {
        String userEmail = authentication.getName();
        ChatMessageResponse response = chatService.sendMessage(userEmail, request);
        return ResponseEntity.ok(ApiResponse.ok("Message sent successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatHistory(Authentication authentication) {
        String userEmail = authentication.getName();
        List<ChatMessageResponse> history = chatService.getChatHistory(userEmail);
        return ResponseEntity.ok(ApiResponse.ok("Chat history retrieved successfully", history));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearChatHistory(Authentication authentication) {
        String userEmail = authentication.getName();
        chatService.clearChatHistory(userEmail);
        return ResponseEntity.ok(ApiResponse.ok("Chat history cleared successfully", null));
    }
}