package ru.aasmc.chatapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.aasmc.chatapp.dto.Subscription;
import ru.aasmc.chatapp.dto.Subscription.Status;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserService userService;
    private final StripeGateway stripeGateway;
    private final LockProvider lockProvider;

    @Scheduled(cron = "*/30 * * * * *")
    public void checkSubscriptions() {
        // Ensures that only one instance of the application is performing
        // the scheduled task. If the instance executing the task fails during
        // the execution, there will be no fail over.
        // need another table in DB. For Postgres:
        // name, lock_until, locked_at, locked_by (see migrations for shedlock)
        LockConfiguration lockCfg = new LockConfiguration(
                Instant.now(),
                "checkSubscriptions",
                Duration.ofSeconds(20),
                Duration.ofSeconds(10)
        );
        lockProvider.lock(lockCfg).ifPresent(lock -> {
            log.info("Checking subscriptions...");

            try {
                stripeGateway.getSubscriptions().forEach(sub -> {
                    userService.getById(sub.getUserId()).ifPresent(user -> {
                        if (sub.getStatus() == Status.ACTIVE && user.isExpired()) {
                            userService.updateUser(user.setExpired(false));
                        }
                        if (sub.getStatus() == Status.PAST_DUE && !user.isExpired()) {
                            userService.updateUser(user.setExpired(true));
                        }                    });
                });
            } finally {
                lock.unlock();
            }
        });
    }
}
