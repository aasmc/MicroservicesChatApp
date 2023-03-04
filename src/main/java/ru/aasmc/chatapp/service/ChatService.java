package ru.aasmc.chatapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.aasmc.chatapp.dto.ChatMessage;
import ru.aasmc.chatapp.repository.ChatMessageRepository;
import ru.aasmc.chatapp.utils.Utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepo;

    @Cacheable(value = "chatMessages")
    public List<ChatMessage> getLastMessages(String chatId) {
        return chatMessageRepo.findAllByChatIdOrderByCreatedAtDesc(chatId, PageRequest.of(0, 10))
                .stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "chatMessages", key = "#result.chatId")
    public ChatMessage saveMessage(Long authorId, Long contactId, String text) {
        String chatId = Utils.chatId(authorId, contactId);
        ChatMessage msg = new ChatMessage()
                .setChatId(chatId)
                .setAuthorId(authorId)
                .setText(text);
        return chatMessageRepo.save(msg);
    }

    public List<ChatMessage> getUnprocessedMessages() {
        return null;
    }
}
