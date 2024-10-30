package com.ukma.competition.platform.config;

import com.ukma.competition.platform.users.UserResponseValidator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AuthenticationTestConfig {

    @Bean
    public UserResponseValidator userResponseValidator() {
        return new UserResponseValidator();
    }
}
