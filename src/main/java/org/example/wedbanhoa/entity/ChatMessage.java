package org.example.wedbanhoa.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "client_email", length = 100)
    private String clientEmail;

    @Column(name = "sender_role", length = 20)
    private String senderRole; // "client" hoặc "admin"

    @Column(name = "message_text", columnDefinition = "NVARCHAR(MAX)")
    private String messageText;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
