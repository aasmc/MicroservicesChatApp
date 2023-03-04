package ru.aasmc.chatapp.confg;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import ru.aasmc.chatapp.dto.User;
import ru.aasmc.chatapp.dto.UserResponse;
import ru.aasmc.chatapp.service.SessionService;
import ru.aasmc.chatapp.utils.Utils;

import java.util.List;

public class WebSocketConnectionHandler extends WebSocketHandlerDecorator {
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionService sessionService;

    public WebSocketConnectionHandler(WebSocketHandler delegate,
                                      SimpMessagingTemplate messagingTemplate,
                                      SessionService sessionService) {
        super(delegate);

        this.messagingTemplate = messagingTemplate;
        this.sessionService = sessionService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        User user = Utils.user(session.getPrincipal());

        if (user != null) {
            Object payload = List.of(new UserResponse(user, true));

            sessionService.getContacts(user.getId()).stream()
                    .filter(UserResponse::isOnline)
                    .forEach(contact -> {
                        messagingTemplate.convertAndSendToUser(
                                contact.getId().toString(),
                                "/topic/contacts", payload);
                    });
        }
    }
}
