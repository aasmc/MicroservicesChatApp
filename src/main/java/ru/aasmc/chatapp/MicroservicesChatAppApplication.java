package ru.aasmc.chatapp;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableSchedulerLock(defaultLockAtLeastFor = "5m", defaultLockAtMostFor = "5m")
@EnableJdbcRepositories
@SpringBootApplication
public class MicroservicesChatAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicesChatAppApplication.class, args);
    }

}
