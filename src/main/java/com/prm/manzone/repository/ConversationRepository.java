package com.prm.manzone.repository;

import com.prm.manzone.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    List<Conversation> findByUserId(Integer userId);
}

