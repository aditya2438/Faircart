package com.faircart.service;

import com.faircart.dto.chat.ChatMessageRequest;
import com.faircart.dto.chat.ChatMessageResponse;
import com.faircart.entity.ChatMessage;
import com.faircart.entity.User;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.ChatMessageRepository;
import com.faircart.repository.UserRepository;
import com.faircart.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatRepository;
    private final UserRepository userRepository;
    private final RecommendationService recommendationService;

    @Transactional
    public ChatMessageResponse sendMessage(String userEmail, ChatMessageRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        // Save user message
        ChatMessage userMessage = ChatMessage.builder()
                .user(user)
                .role(ChatMessage.Role.USER)
                .content(request.getContent())
                .metadata(generateMetadata("user_message"))
                .build();
        chatRepository.save(userMessage);

        // Process with AI (in production, call Spring AI / LLM)
        AIResponse aiResponse = processWithAI(user, request.getContent());

        // Save AI response
        ChatMessage aiMessage = ChatMessage.builder()
                .user(user)
                .role(ChatMessage.Role.ASSISTANT)
                .content(aiResponse.getContent())
                .aiModelUsed(aiResponse.getModelUsed())
                .tokensUsed(aiResponse.getTokensUsed())
                .responseTimeMs(aiResponse.getResponseTimeMs())
                .metadata(aiResponse.getMetadata())
                .build();
        chatRepository.save(aiMessage);

        return ChatMessageResponse.from(aiMessage);
    }

    public List<ChatMessageResponse> getChatHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        List<ChatMessage> messages = chatRepository.findByUserOrderByCreatedAtAsc(user);
        return ChatMessageResponse.from(messages);
    }

    @Transactional
    public void clearChatHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        chatRepository.deleteByUser(user);
    }

    private AIResponse processWithAI(User user, String userMessage) {
        long startTime = System.currentTimeMillis();
        
        // Check if it's a product recommendation query
        if (isRecommendationQuery(userMessage)) {
            RecommendationService.RecommendationResult result = 
                    recommendationService.processAIQuery(user.getEmail(), userMessage);
            
            String response = formatRecommendationResponse(result);
            return AIResponse.builder()
                    .content(response)
                    .modelUsed("FairCart-Recommendation-Engine-v1")
                    .tokensUsed(estimateTokens(response))
                    .responseTimeMs(System.currentTimeMillis() - startTime)
                    .metadata(generateMetadata("recommendation", result))
                    .build();
        }

        // General chat response
        String response = generateGeneralResponse(userMessage);
        return AIResponse.builder()
                .content(response)
                .modelUsed("FairCart-Chat-v1")
                .tokensUsed(estimateTokens(response))
                .responseTimeMs(System.currentTimeMillis() - startTime)
                .metadata(generateMetadata("general_chat"))
                .build();
    }

    private boolean isRecommendationQuery(String message) {
        String lower = message.toLowerCase();
        return lower.contains("recommend") || lower.contains("suggest") || 
               lower.contains("best") || lower.contains("under") || 
               lower.contains("budget") || lower.contains("buy") ||
               lower.contains("compare") || lower.contains("vs");
    }

    private String formatRecommendationResponse(RecommendationService.RecommendationResult result) {
        StringBuilder sb = new StringBuilder();
        
        if (result.getBestBudgetMatch() != null) {
            var best = result.getBestBudgetMatch();
            sb.append("Based on your query, here's my top recommendation:\n\n");
            sb.append("🏆 **").append(best.getProductName()).append("**\n");
            sb.append("💰 **Price:** ₹").append(best.getLowestPrice()).append("\n");
            sb.append("⭐ **Overall Score:** ").append(best.getCompositeScore()).append("/100\n");
            sb.append("✅ **Verdict:** ").append(result.getVerdictLabel()).append("\n\n");
            
            sb.append("**Score Breakdown:**\n");
            sb.append("- Price Value: ").append(best.getPriceScore()).append("/100\n");
            sb.append("- Ratings: ").append(best.getRatingScore()).append("/100\n");
            sb.append("- Review Sentiment: ").append(best.getSentimentScore()).append("/100\n");
            sb.append("- Seller Trust: ").append(best.getSellerScore()).append("/100\n");
            sb.append("- Availability: ").append(best.getAvailabilityScore()).append("/100\n");
            sb.append("- Price History: ").append(best.getPriceHistoryScore()).append("/100\n\n");
        }

        if (result.getSmartStretchUpgrade() != null) {
            var stretch = result.getSmartStretchUpgrade();
            sb.append("💡 **SMART STRETCH UPGRADE**\n");
            sb.append("Consider spending **₹").append(stretch.getPriceIncreasePercent()).append("% more** for:\n");
            sb.append("**").append(stretch.getUpgradeProductName()).append("** at ₹").append(stretch.getUpgradePrice()).append("\n");
            sb.append(stretch.getReasoning()).append("\n\n");
        }

        if (!result.getAllBudgetOptions().isEmpty()) {
            sb.append("**Other Great Options:**\n");
            for (int i = 0; i < Math.min(3, result.getAllBudgetOptions().size()); i++) {
                var opt = result.getAllBudgetOptions().get(i);
                sb.append("• **").append(opt.getProductName()).append("** - ₹").append(opt.getLowestPrice())
                        .append(" (Score: ").append(opt.getCompositeScore()).append("/100)\n");
            }
        }

        sb.append("\n💬 Want me to compare specific products or check price history?");
        return sb.toString();
    }

    private String generateGeneralResponse(String message) {
        String lower = message.toLowerCase();
        
        if (lower.contains("hello") || lower.contains("hi") || lower.contains("hey")) {
            return "Hello! 👋 I'm your FairCart AI Shopping Assistant. I can help you:\n" +
                    "• Find the best products within your budget\n" +
                    "• Compare products across Amazon, Flipkart, Tata Neu, and more\n" +
                    "• Analyze reviews and detect fake ones\n" +
                    "• Track price drops and alert you\n" +
                    "• Find smart upgrade recommendations\n\n" +
                    "What are you looking for today?";
        }
        
        if (lower.contains("how") && lower.contains("work")) {
            return "FairCart works by:\n\n" +
                    "1. **Aggregating** real-time prices from multiple platforms\n" +
                    "2. **Analyzing** thousands of reviews with AI to detect fake ones\n" +
                    "3. **Scoring** products on Price, Ratings, Sentiment, Seller Trust, Availability, and Price History\n" +
                    "4. **Recommending** the best value with clear **BUY NOW / WAIT / SKIP** verdicts\n" +
                    "5. **Smart Stretch**: If spending 10-25% more gets you disproportionately better value, I'll highlight it!\n\n" +
                    "Just tell me what you need and your budget!";
        }

        if (lower.contains("track") || lower.contains("alert") || lower.contains("price drop")) {
            return "I can track products and alert you when prices drop! Just:\n" +
                    "1. Find a product you like\n" +
                    "2. Set your target price\n" +
                    "3. Choose notification method (Email/SMS)\n\n" +
                    "I'll monitor it 24/7 and notify you the moment it drops below your target.";
        }

        return "I'm here to help you make smart shopping decisions! Try asking me:\n" +
                "• \"Best noise-cancelling earbuds under ₹3000\"\n" +
                "• \"Compare iPhone 15 vs OnePlus 12\"\n" +
                "• \"Should I buy Sony WH-1000XM5 now or wait?\"\n" +
                "• \"Track price for MacBook Air M2\"\n\n" +
                "What's on your mind?";
    }

    private int estimateTokens(String text) {
        return text.length() / 4; // Rough approximation
    }

    private String generateMetadata(String type) {
        return generateMetadata(type, null);
    }

    private String generateMetadata(String type, Object data) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.Map<String, Object> meta = new java.util.HashMap<>();
            meta.put("type", type);
            meta.put("timestamp", Instant.now().toString());
            meta.put("sessionId", UUID.randomUUID().toString());
            if (data != null) {
                meta.put("data", data);
            }
            return mapper.writeValueAsString(meta);
        } catch (Exception e) {
            return "{\"type\":\"" + type + "\"}";
        }
    }

    private static class AIResponse {
        private String content;
        private String modelUsed;
        private Integer tokensUsed;
        private Long responseTimeMs;
        private String metadata;

        public AIResponse() {}
        public AIResponse(String content, String modelUsed, Integer tokensUsed, Long responseTimeMs, String metadata) {
            this.content = content;
            this.modelUsed = modelUsed;
            this.tokensUsed = tokensUsed;
            this.responseTimeMs = responseTimeMs;
            this.metadata = metadata;
        }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getModelUsed() { return modelUsed; }
        public void setModelUsed(String modelUsed) { this.modelUsed = modelUsed; }
        public Integer getTokensUsed() { return tokensUsed; }
        public void setTokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; }
        public Long getResponseTimeMs() { return responseTimeMs; }
        public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
        public String getMetadata() { return metadata; }
        public void setMetadata(String metadata) { this.metadata = metadata; }

        public static AIResponseBuilder builder() { return new AIResponseBuilder(); }
        public static class AIResponseBuilder {
            private String content;
            private String modelUsed;
            private Integer tokensUsed;
            private Long responseTimeMs;
            private String metadata;
            public AIResponseBuilder content(String content) { this.content = content; return this; }
            public AIResponseBuilder modelUsed(String modelUsed) { this.modelUsed = modelUsed; return this; }
            public AIResponseBuilder tokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; return this; }
            public AIResponseBuilder responseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; return this; }
            public AIResponseBuilder metadata(String metadata) { this.metadata = metadata; return this; }
            public AIResponse build() { return new AIResponse(content, modelUsed, tokensUsed, responseTimeMs, metadata); }
        }
    }
}