package com.faircart.service;

import com.faircart.dto.product.ProductResponse;
import com.faircart.entity.Product;
import com.faircart.entity.User;
import com.faircart.entity.Wishlist;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.ProductRepository;
import com.faircart.repository.UserRepository;
import com.faircart.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProductResponse addToWishlist(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            throw new IllegalArgumentException("Product already in wishlist");
        }

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();
        wishlistRepository.save(wishlist);

        return ProductResponse.from(product);
    }

    public List<ProductResponse> getWishlist(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        List<Wishlist> wishlistItems = wishlistRepository.findByUser(user);
        return wishlistItems.stream()
                .map(item -> ProductResponse.from(item.getProduct()))
                .collect(Collectors.toList());
    }

    public boolean isInWishlist(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return wishlistRepository.existsByUserAndProduct(user, product);
    }

    @Transactional
    public void removeFromWishlist(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        wishlistRepository.deleteByUserAndProduct(user, product);
    }

    public long getWishlistCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        return wishlistRepository.countByUser(user);
    }
}