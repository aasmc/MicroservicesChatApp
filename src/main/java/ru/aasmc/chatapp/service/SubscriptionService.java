package ru.aasmc.chatapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.aasmc.chatapp.dto.Subscription;
import ru.aasmc.chatapp.dto.Subscription.Status;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserService userService;
    private final StripeGateway stripeGateway;

    @Scheduled(cron = "*/30 * * * * *")
    public void checkSubscriptions() {
        log.info("Checking subscriptions...");
        stripeGateway.getSubscriptions().forEach(sub -> {
            userService.getById(sub.getUserId()).ifPresent(user -> {
                if (sub.getStatus() == Status.ACTIVE && user.isExpired()) {
                    userService.updateUser(user.setExpired(false));
                }
                if (sub.getStatus() == Status.PAST_DUE && !user.isExpired()) {
                    userService.updateUser(user.setExpired(true));
                }
            });
        });
    }
}
