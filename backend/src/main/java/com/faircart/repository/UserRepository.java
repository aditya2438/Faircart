package com.faircart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faircart.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
