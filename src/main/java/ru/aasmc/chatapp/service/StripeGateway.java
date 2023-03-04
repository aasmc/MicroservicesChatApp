package ru.aasmc.chatapp.service;

import org.springframework.stereotype.Component;
import ru.aasmc.chatapp.dto.Subscription;

import java.util.List;

@Component
public class StripeGateway {
    public Iterable<Subscription> getSubscriptions() {
        return List.of();
    }
}
