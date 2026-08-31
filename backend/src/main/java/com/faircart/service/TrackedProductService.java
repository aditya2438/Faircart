package com.faircart.service;

import com.faircart.dto.tracked.TrackedProductRequest;
import com.faircart.dto.tracked.TrackedProductResponse;
import com.faircart.entity.Product;
import com.faircart.entity.ProductPlatformListing;
import com.faircart.entity.TrackedProduct;
import com.faircart.entity.User;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.ProductRepository;
import com.faircart.repository.TrackedProductRepository;
import com.faircart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackedProductService {

    private final TrackedProductRepository trackedRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public TrackedProductResponse trackProduct(String userEmail, TrackedProductRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        // Check if already tracking
        return trackedRepository.findByUserAndProductAndPlatform(
                user, product, ProductPlatformListing.Platform.valueOf(request.getPlatform().toUpperCase()))
                .map(existing -> {
                    existing.setTargetPrice(request.getTargetPrice());
                    existing.setAlertEnabled(request.getAlertEnabled());
                    existing.setNotificationChannel(request.getNotificationChannel());
                    return TrackedProductResponse.from(trackedRepository.save(existing));
                })
                .orElseGet(() -> {
                    TrackedProduct tracked = TrackedProduct.builder()
                            .user(user)
                            .product(product)
                            .platform(ProductPlatformListing.Platform.valueOf(request.getPlatform().toUpperCase()))
                            .targetPrice(request.getTargetPrice())
                            .currentPrice(product.getPrice())
                            .alertEnabled(request.getAlertEnabled())
                            .notificationChannel(request.getNotificationChannel())
                            .build();
                    return TrackedProductResponse.from(trackedRepository.save(tracked));
                });
    }

    public List<TrackedProductResponse> getUserTrackedProducts(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        List<TrackedProduct> tracked = trackedRepository.findByUser(user);
        return TrackedProductResponse.from(tracked);
    }

    public List<TrackedProductResponse> getUserActiveTrackedProducts(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        List<TrackedProduct> tracked = trackedRepository.findByUserAndAlertEnabledTrue(user);
        return TrackedProductResponse.from(tracked);
    }

    @Transactional
    public TrackedProductResponse updateTrackedProduct(Long trackedId, TrackedProductRequest request) {
        TrackedProduct tracked = trackedRepository.findById(trackedId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracked product not found with id: " + trackedId));

        tracked.setTargetPrice(request.getTargetPrice());
        tracked.setAlertEnabled(request.getAlertEnabled());
        tracked.setNotificationChannel(request.getNotificationChannel());

        return TrackedProductResponse.from(trackedRepository.save(tracked));
    }

    @Transactional
    public void deleteTrackedProduct(Long trackedId) {
        trackedRepository.deleteById(trackedId);
    }

    @Transactional
    public void checkAndNotifyPriceDrops() {
        List<TrackedProduct> activeTracked = trackedRepository.findByAlertEnabledTrue();
        
        for (TrackedProduct tracked : activeTracked) {
            BigDecimal currentPrice = getCurrentEffectivePrice(tracked);
            if (currentPrice != null) {
                tracked.setCurrentPrice(currentPrice);
                tracked.setLastCheckedAt(Instant.now());
                
                if (currentPrice.compareTo(tracked.getTargetPrice()) <= 0) {
                    // Price dropped below target - send notification
                    sendPriceDropNotification(tracked, currentPrice);
                    tracked.setLastNotifiedAt(Instant.now());
                }
                
                trackedRepository.save(tracked);
            }
        }
    }

    private BigDecimal getCurrentEffectivePrice(TrackedProduct tracked) {
        // In production, this would call the scraper service
        // For now, return a mock price
        return tracked.getProduct().getPrice();
    }

    private void sendPriceDropNotification(TrackedProduct tracked, BigDecimal currentPrice) {
        // In production, this would integrate with Email/SMS/WhatsApp services
        System.out.println("PRICE DROP ALERT: " + tracked.getProduct().getName() + 
                " dropped to " + currentPrice + " (target: " + tracked.getTargetPrice() + ")");
    }

    public List<TrackedProduct> getProductsBelowTarget(BigDecimal price) {
        return trackedRepository.findByTargetPriceLessThanEqualAndAlertEnabledTrue(price);
    }
}