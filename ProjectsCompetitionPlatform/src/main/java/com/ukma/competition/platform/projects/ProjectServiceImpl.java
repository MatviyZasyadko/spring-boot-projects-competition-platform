package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.projects.dto.ProjectCreateDto;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ProjectServiceImpl extends GenericServiceImpl<ProjectEntity, String, ProjectRepository>
    implements ProjectService {

    @Value("${spring.cloudinary.folder}")
    String cloudinaryFolder;

    UserService userService;
    CloudinaryService cloudinaryService;

    @Autowired
    public ProjectServiceImpl(
        ProjectRepository repository,
        UserService userService,
        CloudinaryService cloudinaryService
    ) {
        super(repository);
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    @Transactional
    public void saveFromDto(ProjectCreateDto projectCreateDto, String userEmail) throws Exception {
        try {
            UserEntity projectCreator = userService.findByEmail(userEmail).orElseThrow();
            ProjectEntity project = ProjectEntity.builder()
                .shortDescription(projectCreateDto.getShortDescription())
                .fullDescription(projectCreateDto.getFullDescription())
                .name(projectCreateDto.getName())
                .creator(projectCreator)
                .build();

            if (projectCreateDto.getLogo() != null && !projectCreateDto.getLogo().isEmpty()) {
                saveImage(project, projectCreateDto.getLogo(), true);
            }
            for (MultipartFile image : projectCreateDto.getImages()) {
                saveImage(project, image, false);
            }

            super.save(project);
            log.info("Project entity with id {} was successfully created.", project.getId());
        } catch (Exception exception) {
            log.error("Error occured while saving a project: {}", exception.getMessage());
            throw exception;
        }
    }

    private void saveImage(ProjectEntity project, MultipartFile image, boolean isMain) throws IOException {
        String publicUrl = cloudinaryService.upload(image, cloudinaryFolder);
        ImageEntity logo = ImageEntity.builder()
            .url(publicUrl)
            .isMain(isMain)
            .name(image.getOriginalFilename())
            .build();
        project.addImage(logo);
    }

    @Scheduled(fixedRate = 900000) // every 15 minutes
    public void cacheTopProjects() {
        // Fetch top 10 projects and store them in an in-memory cache
        log.info("Updated cache for top projects.");
    }
}
