package com.ukma.competition.platform;

import com.ukma.competition.platform.competitions.database_layer.CompetitionRepository;
import com.ukma.competition.platform.projects.ProjectRepository;
import com.ukma.competition.platform.users.UserRepository;
import com.ukma.competition.platform.votes.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class ProjectsCompetitionPlatformApplication {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    CompetitionRepository competitionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ProjectsCompetitionPlatformApplication.class, args);
    }
}
