package org.example.wedbanhoa.repository;

import org.example.wedbanhoa.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    List<ChatMessage> findByClientIdOrderByCreatedAtAsc(Integer clientId);

    long countByClientIdAndSenderRoleAndIsReadFalse(Integer clientId, String senderRole);

    long countBySenderRoleAndIsReadFalse(String senderRole);

    @Query("SELECT DISTINCT m.clientId, m.clientEmail FROM ChatMessage m WHERE m.clientId IS NOT NULL ORDER BY m.clientId DESC")
    List<Object[]> findDistinctClients();

    @Modifying
    @Query("UPDATE ChatMessage m SET m.isRead = true WHERE m.clientId = :clientId AND m.senderRole = :senderRole AND (m.isRead = false OR m.isRead IS NULL)")
    void markMessagesAsRead(@Param("clientId") Integer clientId, @Param("senderRole") String senderRole);
}
