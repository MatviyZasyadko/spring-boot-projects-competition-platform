package com.ukma.competition.platform;

import com.ukma.competition.platform.auth.oauth.AuthenticationProvider;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.competitions.database_layer.CompetitionRepository;
import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.projects.ProjectRepository;
import com.ukma.competition.platform.users.UserRole;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserRepository;
import com.ukma.competition.platform.votes.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;

@SpringBootApplication
@EnableScheduling
public class ProjectsCompetitionPlatformApplication implements CommandLineRunner {

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

    @Override
    public void run(String... args) {
      //  UserEntity admin = UserEntity.builder()
      //      .email("admin@mail.com")
      //      .fullName("Volodymyr Havryliuk")
      //      .password(passwordEncoder.encode("admin"))
      //      .userRole(UserRole.ADMIN)
      //      .authenticationProvider(AuthenticationProvider.NATIVE)
      //      .build();
//
      //  ProjectEntity firstProject = ProjectEntity.builder()
      //      .name("Competitors")
      //      .shortDescription("Competitors is an innovative platform where users can showcase their projects, " +
      //                        "participate in various competitions, and win exciting prizes. Designed for creators, developers, and innovators.")
      //      .fullDescription("Competitors is an innovative platform where users can showcase their projects, " +
      //                       "participate in various competitions, and win exciting prizes. " +
      //                       "Designed for creators, developers, and innovators, " +
      //                       "it allows users to create detailed profiles for their projects, including descriptions, images, and key features.")
      //      .images(
      //          new ArrayList<>() {{
      //              add(
      //                  ImageEntity.builder().url("https://static.vecteezy.com/system/resources/previews/008/296/131/non_2x/trophy-icon-isolated-on-white-background-free-vector.jpg")
      //                      .main(true)
      //                      .name("somename.jpg").build()
      //              );
      //          }}
      //      )
      //      .creator(admin)
      //      .build();
//
      //  ProjectEntity secondProject = ProjectEntity.builder()
      //      .name("Volo")
      //      .shortDescription("Volo is a web platform designed to help individuals and organizations raise funds for " +
      //                        "charitable causes. It simplifies the donation process, allowing users to create campaigns," +
      //                        " share them with others, and collect contributions securely")
      //      .fullDescription("Competitors is an innovative platform where users can showcase their projects, " +
      //                       "participate in various competitions, and win exciting prizes. " +
      //                       "Designed for creators, developers, and innovators, " +
      //                       "it allows users to create detailed profiles for their projects, including descriptions, images, and key features.")
      //      .images(
      //          new ArrayList<>() {{
      //              add(
      //                  ImageEntity.builder().url("https://voloapparel.com/cdn/shop/files/instagramlogo5_480x.jpg?v=1614245885")
      //                      .name("somename.jpg")
      //                      .main(true)
      //                      .build()
      //              );
      //              add(
      //                  ImageEntity.builder().url("https://voloapparel.com/cdn/shop/files/instagramlogo5_480x.jpg?v=1614245885")
      //                      .name("somename.jpg")
      //                      .main(false)
      //                      .build()
      //              );
//
      //              add(
      //                  ImageEntity.builder().url("https://voloapparel.com/cdn/shop/files/instagramlogo5_480x.jpg?v=1614245885")
      //                      .name("somename.jpg")
      //                      .main(false)
      //                      .build()
      //              );
//
      //              add(
      //                  ImageEntity.builder().url("https://voloapparel.com/cdn/shop/files/instagramlogo5_480x.jpg?v=1614245885")
      //                      .name("somename.jpg")
      //                      .main(false)
      //                      .build()
      //              );
      //          }}
      //      )
      //      .creator(admin)
      //      .build();
//
      //  admin.addProject(firstProject);
      //  admin.addProject(secondProject);
      //  for (int i = 0; i < 19; i++) {
      //      admin.addProject(firstProject);
      //      firstProject = ProjectEntity.builder()
      //          .name("Competitors")
      //          .shortDescription("Competitors is an innovative platform where users can showcase their projects, " +
      //                            "participate in various competitions, and win exciting prizes. Designed for creators, developers, and innovators.")
      //          .fullDescription("Competitors is an innovative platform where users can showcase their projects, " +
      //                           "participate in various competitions, and win exciting prizes. " +
      //                           "Designed for creators, developers, and innovators, " +
      //                           "it allows users to create detailed profiles for their projects, including descriptions, images, and key features.")
      //          .images(
      //              new ArrayList<>() {{
      //                  add(
      //                      ImageEntity.builder().url("https://static.vecteezy.com/system/resources/previews/008/296/131/non_2x/trophy-icon-isolated-on-white-background-free-vector.jpg")
      //                          .main(true)
      //                          .name("somename.jpg").build()
      //                  );
      //                  add(
      //                      ImageEntity.builder().url("https://static.vecteezy.com/system/resources/previews/008/296/131/non_2x/trophy-icon-isolated-on-white-background-free-vector.jpg")
      //                          .main(false)
      //                          .name("somename.jpg").build()
      //                  );
      //                  add(
      //                      ImageEntity.builder().url("https://static.vecteezy.com/system/resources/previews/008/296/131/non_2x/trophy-icon-isolated-on-white-background-free-vector.jpg")
      //                          .main(false)
      //                          .name("somename.jpg").build()
      //                  );
      //              }}
      //          )
      //          .creator(admin)
      //          .build();
      //  }
      //  if (userRepository != null) {
      //      userRepository.save(admin);
      //  }
//
      //  CompetitionEntity competitionEntity = CompetitionEntity.builder()
      //      .name("first competition")
      //      .description("first competition description")
      //      .votingEndDate(Instant.MAX)
      //      .organizer(admin)
      //      .build();
//
      //  competitionRepository.saveAndFlush(competitionEntity);
    }
}
