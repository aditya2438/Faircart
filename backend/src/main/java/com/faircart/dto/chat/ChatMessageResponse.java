package com.faircart.dto.chat;

import com.faircart.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {

    private Long id;
    private Long userId;
    private String role;
    private String content;
    private String aiModelUsed;
    private Integer tokensUsed;
    private Long responseTimeMs;
    private String metadata;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAiModelUsed() { return aiModelUsed; }
    public void setAiModelUsed(String aiModelUsed) { this.aiModelUsed = aiModelUsed; }
    public Integer getTokensUsed() { return tokensUsed; }
    public void setTokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; }
    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static ChatMessageResponseBuilder builder() { return new ChatMessageResponseBuilder(); }
    public static class ChatMessageResponseBuilder {
        private Long id;
        private Long userId;
        private String role;
        private String content;
        private String aiModelUsed;
        private Integer tokensUsed;
        private Long responseTimeMs;
        private String metadata;
        private Instant createdAt;
        private Instant updatedAt;

        public ChatMessageResponseBuilder id(Long id) { this.id = id; return this; }
        public ChatMessageResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public ChatMessageResponseBuilder role(String role) { this.role = role; return this; }
        public ChatMessageResponseBuilder content(String content) { this.content = content; return this; }
        public ChatMessageResponseBuilder aiModelUsed(String aiModelUsed) { this.aiModelUsed = aiModelUsed; return this; }
        public ChatMessageResponseBuilder tokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; return this; }
        public ChatMessageResponseBuilder responseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; return this; }
        public ChatMessageResponseBuilder metadata(String metadata) { this.metadata = metadata; return this; }
        public ChatMessageResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public ChatMessageResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public ChatMessageResponse build() {
            ChatMessageResponse c = new ChatMessageResponse();
            c.setId(id);
            c.setUserId(userId);
            c.setRole(role);
            c.setContent(content);
            c.setAiModelUsed(aiModelUsed);
            c.setTokensUsed(tokensUsed);
            c.setResponseTimeMs(responseTimeMs);
            c.setMetadata(metadata);
            c.setCreatedAt(createdAt);
            c.setUpdatedAt(updatedAt);
            return c;
        }
    }

    public static ChatMessageResponse from(ChatMessage message) {
        if (message == null) return null;
        return ChatMessageResponse.builder()
                .id(message.getId())
                .userId(message.getUser() != null ? message.getUser().getId() : null)
                .role(message.getRole() != null ? message.getRole().name() : null)
                .content(message.getContent())
                .aiModelUsed(message.getAiModelUsed())
                .tokensUsed(message.getTokensUsed())
                .responseTimeMs(message.getResponseTimeMs())
                .metadata(message.getMetadata())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    public static List<ChatMessageResponse> from(List<ChatMessage> messages) {
        if (messages == null) return List.of();
        return messages.stream()
                .map(ChatMessageResponse::from)
                .toList();
    }
}