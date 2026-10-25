package org.example.wedbanhoa.service;

import org.example.wedbanhoa.entity.ChatMessage;
import org.example.wedbanhoa.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public ChatMessage saveMessage(Integer clientId, String clientEmail, String senderRole, String text) {
        if (text == null || text.trim().isEmpty()) return null;
        ChatMessage msg = ChatMessage.builder()
                .clientId(clientId)
                .clientEmail(clientEmail)
                .senderRole(senderRole != null ? senderRole.toLowerCase() : "client")
                .messageText(text.trim())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(msg);
    }

    @Transactional
    public List<Map<String, Object>> getMessages(Integer clientId, String viewerRole) {
        if (clientId == null) return Collections.emptyList();

        // Nếu admin xem -> đánh dấu tin nhắn của client là đã đọc
        if ("admin".equalsIgnoreCase(viewerRole)) {
            chatMessageRepository.markMessagesAsRead(clientId, "client");
        } else {
            // Nếu client xem -> đánh dấu tin nhắn của admin là đã đọc
            chatMessageRepository.markMessagesAsRead(clientId, "admin");
        }

        List<ChatMessage> list = chatMessageRepository.findByClientIdOrderByCreatedAtAsc(clientId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (ChatMessage m : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getMessageId());
            map.put("role", m.getSenderRole());
            map.put("text", m.getMessageText());
            map.put("created_at", m.getCreatedAt() != null ? m.getCreatedAt().format(FORMATTER) : "");
            result.add(map);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getChatUsers() {
        List<Object[]> distinctClients = chatMessageRepository.findDistinctClients();
        List<Map<String, Object>> users = new ArrayList<>();
        for (Object[] row : distinctClients) {
            Integer cId = (Integer) row[0];
            String email = (String) row[1];
            long unread = chatMessageRepository.countByClientIdAndSenderRoleAndIsReadFalse(cId, "client");

            Map<String, Object> u = new HashMap<>();
            u.put("clientId", cId);
            u.put("email", (email != null && !email.isEmpty()) ? email : ("Khách #" + cId));
            u.put("unread", unread);
            users.add(u);
        }
        return users;
    }

    @Transactional(readOnly = true)
    public long getUnreadCountForAdmin() {
        return chatMessageRepository.countBySenderRoleAndIsReadFalse("client");
    }

    @Transactional(readOnly = true)
    public long getUnreadCountForClient(Integer clientId) {
        if (clientId == null) return 0;
        return chatMessageRepository.countByClientIdAndSenderRoleAndIsReadFalse(clientId, "admin");
    }
}
