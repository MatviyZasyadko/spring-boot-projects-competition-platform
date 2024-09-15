package com.SpringBoot.ProjectsCompetitionPlatform.config;

import com.SpringBoot.ProjectsCompetitionPlatform.projects.ProjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ProjectRepository projectRepository() {
        return new ProjectRepository();
    }
}
