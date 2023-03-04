package ru.aasmc.chatapp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.aasmc.chatapp.dto.ChatMessage;

import java.util.List;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatIdOrderByCreatedAtDesc(String chatKd, Pageable pageable);
}
