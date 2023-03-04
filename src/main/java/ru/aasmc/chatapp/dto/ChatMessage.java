package ru.aasmc.chatapp.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("chat_messages")
public class ChatMessage {
    @Id
    private Long id;
    private String chatId;
    private Long authorId;
    private String text;
    private LocalDateTime createdAt = LocalDateTime.now();
}
