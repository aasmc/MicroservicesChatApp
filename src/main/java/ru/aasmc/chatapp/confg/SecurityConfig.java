package ru.aasmc.chatapp.confg;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSecurity
@EnableSpringHttpSession
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .deleteCookies("SESSION")
                .and()
                .csrf()
                .disable();

        return http.build();
    }

    @Bean
    public Map<String, Session> sessionMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public SessionRepository sessionRepository() {
        return new MapSessionRepository(sessionMap());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
