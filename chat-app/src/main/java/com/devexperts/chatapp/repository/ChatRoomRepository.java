package com.devexperts.chatapp.repository;


import com.devexperts.chatapp.model.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    Optional<ChatRoomEntity> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
