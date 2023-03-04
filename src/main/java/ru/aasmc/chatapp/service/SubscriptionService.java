package ru.aasmc.chatapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.aasmc.chatapp.dto.Subscription.Status;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserService userService;
    private final StripeGateway stripeGateway;

    // Ensures that only one instance of the application is performing
    // the scheduled task. If the instance executing the task fails during
    // the execution, there will be no fail over.
    // need another table in DB. For Postgres:
    // name, lock_until, locked_at, locked_by (see migrations for shedlock)
    @SchedulerLock(name = "checkSubscriptions", lockAtLeastFor = "10s", lockAtMostFor = "20s")
    @Scheduled(cron = "*/30 * * * * *")
    public void checkSubscriptions() {
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
