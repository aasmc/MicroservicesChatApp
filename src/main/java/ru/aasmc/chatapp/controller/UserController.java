package ru.aasmc.chatapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aasmc.chatapp.dto.ContactRequest;
import ru.aasmc.chatapp.dto.User;
import ru.aasmc.chatapp.dto.UserResponse;
import ru.aasmc.chatapp.service.SessionService;
import ru.aasmc.chatapp.service.UserService;

import java.util.List;

@Controller
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    @GetMapping("/api/v1/user")
    public UserResponse getUser(@AuthenticationPrincipal User user) {
        return new UserResponse(user, true);
    }

    @PutMapping("/api/v1/contacts")
    public void addContact(@AuthenticationPrincipal User user, ContactRequest req) {
        userService.addContact(user, req.getUsername());
    }

    @SubscribeMapping("/topic/contacts")
    public List<UserResponse> contacts(@AuthenticationPrincipal User user) {
        return sessionService.getContacts(user.getId());
    }
}
