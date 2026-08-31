package com.faircart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "ai_model_used", length = 100)
    private String aiModelUsed;

    @Column(name = "tokens_used")
    private Integer tokensUsed;

    @Column(name = "response_time_ms")
    private Long responseTimeMs;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
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

    public static ChatMessageBuilder builder() { return new ChatMessageBuilder(); }
    public static class ChatMessageBuilder {
        private User user;
        private Role role;
        private String content;
        private String aiModelUsed;
        private Integer tokensUsed;
        private Long responseTimeMs;
        private String metadata;

        public ChatMessageBuilder user(User user) { this.user = user; return this; }
        public ChatMessageBuilder role(Role role) { this.role = role; return this; }
        public ChatMessageBuilder content(String content) { this.content = content; return this; }
        public ChatMessageBuilder aiModelUsed(String aiModelUsed) { this.aiModelUsed = aiModelUsed; return this; }
        public ChatMessageBuilder tokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; return this; }
        public ChatMessageBuilder responseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; return this; }
        public ChatMessageBuilder metadata(String metadata) { this.metadata = metadata; return this; }

        public ChatMessage build() {
            ChatMessage m = new ChatMessage();
            m.setUser(user);
            m.setRole(role);
            m.setContent(content);
            m.setAiModelUsed(aiModelUsed);
            m.setTokensUsed(tokensUsed);
            m.setResponseTimeMs(responseTimeMs);
            m.setMetadata(metadata);
            return m;
        }
    }

    public enum Role {
        USER, ASSISTANT, SYSTEM
    }
}