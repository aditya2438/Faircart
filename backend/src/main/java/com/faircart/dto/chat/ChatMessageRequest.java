package com.faircart.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {

    @NotBlank(message = "Message content is required")
    @Size(max = 5000, message = "Message cannot exceed 5000 characters")
    private String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public static ChatMessageRequestBuilder builder() { return new ChatMessageRequestBuilder(); }
    public static class ChatMessageRequestBuilder {
        private String content;
        public ChatMessageRequestBuilder content(String content) { this.content = content; return this; }
        public ChatMessageRequest build() {
            ChatMessageRequest c = new ChatMessageRequest();
            c.setContent(content);
            return c;
        }
    }
}