package com.ukma.competition.platform.projects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukma.competition.platform.comments.CommentEntity;
import com.ukma.competition.platform.comments.CommentService;
import com.ukma.competition.platform.comments.dto.CommentCreateDto;
import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.projects.dto.ProjectCreateUpdateDto;
import com.ukma.competition.platform.projects.dto.ProjectListDto;
import com.ukma.competition.platform.projects.dto.ProjectListItemDto;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.shared.constants.AppConstants;
import com.ukma.competition.platform.shared.dto.PaginationDto;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import com.ukma.competition.platform.users.dto.UserDto;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.yaml.snakeyaml.events.Event.ID.Comment;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProjectServiceImpl extends GenericServiceImpl<ProjectEntity, String, ProjectRepository>
        implements ProjectService {

    UserService userService;
    CloudinaryService cloudinaryService;
    ObjectMapper objectMapper;
    CommentService commentService;

    @Autowired
    public ProjectServiceImpl(
            ProjectRepository repository,
            UserService userService,
            CloudinaryService cloudinaryService,
            ObjectMapper objectMapper,
            CommentService commentService
    ) {
        super(repository);
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.objectMapper = objectMapper;
        this.commentService = commentService;
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

    public List<ProjectListItemDto> findAllByCreator(UserEntity user) {
        List<ProjectEntity> projectEntities = repository.findAllByCreator(user);
        return projectEntities.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProjectListItemDto findOneAsDtoById(String id) {
        return super.findById(id).map(this::convertToDto).orElseThrow();
    }

    public ProjectListItemDto convertToDto(ProjectEntity project) {
        return new ProjectListItemDto(
                project.getId(),
                project.getName(),
                project.getShortDescription(),
                project.getFullDescription(),
                buildImageResponseDto(project.getLogo()),
                project.getImages().stream().filter(image -> !image.getMain()).map(this::buildImageResponseDto).toList(),
                new UserDto(
                        project.getCreator().getId(),
                        project.getCreator().getFullName(),
                        project.getCreator().getEmail(),
                        project.getCreator().getLogoUrl()
                ),
                project.getCreatedAt(),
                project.getComments().stream()
                        .sorted(Comparator.comparing(CommentEntity::getCreatedAt).reversed())
                        .map(commentService::convertToDto)
                        .toList(),
                0);
    }

    private ImageResponseDto buildImageResponseDto(ImageEntity image) {
        return image == null
                ? null
                : new ImageResponseDto(
                image.getId(),
                image.getCreatedAt(),
                image.getUpdatedAt(),
                image.getUrl(),
                image.getPublicId(),
                image.getName()
        );
    }

    public boolean addComment(CommentCreateDto commentCreateDto) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByEmail(principal.getName()).orElse(null);

        ProjectEntity projectEntity = repository.findById(commentCreateDto.getProjectId()).orElse(null);
        if (projectEntity == null) {
            return false;
        }

        CommentEntity commentEntity = CommentEntity.builder()
                .author(user)
                .text(commentCreateDto.getText())
                .project(projectEntity)
                .build();

        commentService.save(commentEntity);
        return true;
    }

    public ProjectCreateUpdateDto buildUpdateDto(String id) throws JsonProcessingException {
        ProjectEntity project = this.findById(id).orElseThrow();

        ProjectCreateUpdateDto projectCreateUpdateDto = new ProjectCreateUpdateDto();
        projectCreateUpdateDto.setName(project.getName());
        projectCreateUpdateDto.setShortDescription(project.getShortDescription());
        projectCreateUpdateDto.setFullDescription(project.getFullDescription());
        projectCreateUpdateDto.setLogoUrl(project.getLogoUrl());
        projectCreateUpdateDto.setUploadedImagesJson(
                objectMapper.writeValueAsString(
                        project.getImages().stream().filter(image -> !image.getMain())
                                .map(this::buildImageResponseDto)
                                .toList()
                )
        );
        projectCreateUpdateDto.setCreatorId(project.getCreator().getId());
        projectCreateUpdateDto.setUpdate(true);
        projectCreateUpdateDto.setProjectId(id);

        return projectCreateUpdateDto;
    }

    @Transactional
    public void updateCallback(ProjectCreateUpdateDto projectCreateUpdateDto, String id) throws IOException {
        ProjectEntity project = this.findById(id).orElseThrow();

        project.setName(projectCreateUpdateDto.getName());
        project.setShortDescription(projectCreateUpdateDto.getShortDescription());
        project.setFullDescription(projectCreateUpdateDto.getFullDescription());

        if (projectCreateUpdateDto.getLogo() != null) {
            if (project.getLogo() != null) {
                cloudinaryService.remove(project.getLogo().getPublicId(), AppConstants.cloudinaryFolder);
                project.removeImage(project.getLogo());
            }
            saveImage(project, projectCreateUpdateDto.getLogo(), true);
        }

        List<ImageResponseDto> imageResponseDtoList = objectMapper.readValue(projectCreateUpdateDto.getUploadedImagesJson(), new TypeReference<>() {
        });

        for (ImageResponseDto imageFromForm : imageResponseDtoList) {
            boolean imageWasDeleted = !project.getImages()
                    .stream()
                    .filter(image -> !image.getMain())
                    .map(ImageEntity::getId).toList()
                    .contains(imageFromForm.getId());
            if (imageWasDeleted) {
                ImageEntity imageToDelete = project.getImages()
                        .stream()
                        .filter(image -> Objects.equals(image.getId(), imageFromForm.getId()))
                        .findFirst()
                        .orElseThrow();
                cloudinaryService.remove(imageToDelete.getPublicId(), AppConstants.cloudinaryFolder);
                project.removeImage(imageToDelete);
            }
        }

        this.save(project);
    }

    @Override
    @Transactional
    public void saveFromDto(ProjectCreateUpdateDto projectCreateDto, String userEmail) throws Exception {
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
        String publicUrl = cloudinaryService.upload(image, AppConstants.cloudinaryFolder);
        ImageEntity logo = ImageEntity.builder()
                .url(publicUrl)
                .main(isMain)
                .name(image.getOriginalFilename())
                .build();
        project.addImage(logo);
    }
}
