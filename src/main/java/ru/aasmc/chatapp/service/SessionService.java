package ru.aasmc.chatapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.aasmc.chatapp.dto.User;
import ru.aasmc.chatapp.dto.UserResponse;
import ru.aasmc.chatapp.utils.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserService userService;

    private final Map<String, Session> sessionMap;

    public List<UserResponse> getContacts(Long userId) {
        Instant now = Instant.now();
        List<User> contacts = userService.getContacts(userId);
        Map<User, List<Session>> groupedSessions = groupSessions(contacts);
        return contacts.stream()
                .map(user -> {
                    Instant lastAccessedTime = getLastAccessedTime(groupedSessions.get(user));
                    boolean online = Duration.between(lastAccessedTime, now).getSeconds() < 60;
                    return new UserResponse(user, online);
                }).collect(Collectors.toList());
    }

    private Instant getLastAccessedTime(Collection<? extends Session> sessions) {
        Instant lastAccessedTime = Instant.MIN;
        if (CollectionUtils.isEmpty(sessions)) {
            return lastAccessedTime;
        }
        for (Session ses: sessions) {
            if (lastAccessedTime.isBefore(ses.getLastAccessedTime())) {
                lastAccessedTime = ses.getLastAccessedTime();
            }
        }
        return lastAccessedTime;
    }

    private Map<User, List<Session>> groupSessions(List<User> contacts0) {
        Set<User> contacts = new HashSet<>(contacts0);
        Map<User, List<Session>> grouped = new HashMap<>();
        for (Session session : sessionMap.values()) {
            User user = Utils.user(session.getAttribute("SPRING_SECURITY_CONTEXT"));
            if (user == null || !contacts.contains(user)) {
                continue;
            }
            grouped.computeIfAbsent(user, key -> new ArrayList<>())
                    .add(session);
        }
        return grouped;
    }

}
