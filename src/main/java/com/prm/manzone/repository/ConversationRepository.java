package com.prm.manzone.repository;

import com.prm.manzone.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    Optional<Conversation> findByUserId(Integer userId);
    List<Conversation> findAll(); // staff/admin gọi API này để xem danh sách
}

