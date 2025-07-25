package com.prm.manzone.repository;

import com.prm.manzone.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByConversationId(Integer conversationId);
    List<Message> findBySenderId(Long senderId);
    Page<Message> findByConversationId(Integer conversationId, Pageable pageable);
}

