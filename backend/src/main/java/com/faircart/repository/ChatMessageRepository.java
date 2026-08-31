package com.faircart.repository;

import com.faircart.entity.ChatMessage;
import com.faircart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByUserOrderByCreatedAtAsc(User user);

    List<ChatMessage> findByUserOrderByCreatedAtDesc(User user);

    void deleteByUser(User user);
}