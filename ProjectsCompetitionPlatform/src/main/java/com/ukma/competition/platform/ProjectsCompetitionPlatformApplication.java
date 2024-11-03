package com.ukma.competition.platform;

import com.ukma.competition.platform.auth.oauth.AuthenticationProvider;
import com.ukma.competition.platform.users.UserRole;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjectsCompetitionPlatformApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ProjectsCompetitionPlatformApplication.class, args);
    }

    @Override
    public void run(String... args) {
        UserEntity admin = UserEntity.builder()
            .email("admin@mail.com")
            .fullName("VOVAAAAA")
            .password(passwordEncoder.encode("admin"))
            .userRole(UserRole.ADMIN)
            .authenticationProvider(AuthenticationProvider.NATIVE)
            .build();
        if (userRepository != null) {
            userRepository.save(admin);
        }
    }
}
