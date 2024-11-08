package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.projects.dto.ProjectCreateDto;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
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
    public void saveFromDto(ProjectCreateDto projectCreateDto, String userEmail) throws IOException {
        UserEntity projectCreator = userService.findByEmail(userEmail).orElseThrow();
        ProjectEntity project = ProjectEntity.builder()
            .shortDescription(projectCreateDto.getShortDescription())
            .fullDescription(projectCreateDto.getFullDescription())
            .name(projectCreateDto.getName())
            .creator(projectCreator)
            .build();

        if (projectCreateDto.getLogo() != null && !projectCreateDto.getLogo().isEmpty()) {
            String publicUrl = cloudinaryService.upload(projectCreateDto.getLogo(), cloudinaryFolder);
            ImageEntity logo = ImageEntity.builder()
                .url(publicUrl)
                .isMain(true)
                .build();

            project.addImage(logo);
        }

        super.save(project);
    }
}
