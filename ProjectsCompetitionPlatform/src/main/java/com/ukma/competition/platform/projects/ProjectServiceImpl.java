package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.projects.dto.ProjectCreateDto;
import com.ukma.competition.platform.projects.dto.ProjectListDto;
import com.ukma.competition.platform.projects.dto.ProjectRecordDto;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.shared.dto.PaginationDto;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import com.ukma.competition.platform.users.dto.UserDto;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProjectServiceImpl extends GenericServiceImpl<ProjectEntity, String, ProjectRepository>
    implements ProjectService {

    String cloudinaryFolder;
    UserService userService;
    CloudinaryService cloudinaryService;

    @Autowired
    public ProjectServiceImpl(
        ProjectRepository repository,
        UserService userService,
        CloudinaryService cloudinaryService,
        @Value("${spring.cloudinary.folder}")
        String cloudinaryFolder
    ) {
        super(repository);
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.cloudinaryFolder = cloudinaryFolder;
    }

    @Transactional(readOnly = true)
    public ProjectListDto findAllWithSearch(Pageable pageable, String search) {
        Specification<ProjectEntity> specification = null;
        if (StringUtils.isNotBlank(search)) {
            specification = (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%");
        }
        Page<ProjectEntity> projectPage = super.findAll(specification, pageable);
        List<PaginationDto> paginationDtoList = new ArrayList<>();
        if (projectPage.getTotalPages() != 0) {
            IntStream.rangeClosed(1, projectPage.getTotalPages()).forEach(pageNumber -> paginationDtoList.add(
                new PaginationDto(
                    pageNumber,
                    (pageable.getPageNumber() + 1) == pageNumber
                )
            ));
        }

        return new ProjectListDto(
            projectPage.stream().map(this::convertToDto).toList(),
            paginationDtoList,
            projectPage.getTotalPages()
        );
    }

    private ProjectRecordDto convertToDto(ProjectEntity project) {
        return new ProjectRecordDto(
            project.getId(),
            project.getName(),
            project.getShortDescription(),
            project.getFullDescription(),
            new ImageResponseDto(
                project.getLogo().getId(),
                project.getLogo().getCreatedAt(),
                project.getLogo().getUpdatedAt(),
                project.getLogo().getUrl(),
                project.getLogo().getPublicId()
            ),
            new UserDto(
                project.getCreator().getId(),
                project.getCreator().getFullName(),
                project.getCreator().getEmail()
            )
        );
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
            log.error("Error occurred while saving a project: {}", exception.getMessage());
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
}
