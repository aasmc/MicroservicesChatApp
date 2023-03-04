package ru.aasmc.chatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableJdbcRepositories
@SpringBootApplication
public class MicroservicesChatAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicesChatAppApplication.class, args);
    }

}
