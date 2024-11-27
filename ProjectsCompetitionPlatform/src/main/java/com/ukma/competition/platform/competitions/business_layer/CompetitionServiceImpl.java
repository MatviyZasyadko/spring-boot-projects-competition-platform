package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.competitions.database_layer.CompetitionRepository;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.competitions.presentation_layer.CompetitionItemDto;
import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.projects.ProjectService;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.shared.constants.AppConstants;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import com.ukma.competition.platform.users.dto.UserDto;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
@Component
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CompetitionServiceImpl extends GenericServiceImpl<CompetitionEntity, String, CompetitionRepository> implements CompetitionService {

    private static final Marker COMPETITION_MARKER = MarkerManager.getMarker("COMPETITION");
    CompetitionProperties competitionProperties;
    ProjectService projectService;
    UserService userService;
    CloudinaryService cloudinaryService;

    @Autowired
    public CompetitionServiceImpl(
        CompetitionRepository repository,
        CompetitionProperties competitionProperties,
        ProjectService projectService,
        UserService userService,
        CloudinaryService cloudinaryService
    ) {
        super(repository);
        this.competitionProperties = competitionProperties;
        this.projectService = projectService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    public boolean canAddProject(Competition competition) {
        int currentProjects = competition.getProjects().size();
        return currentProjects < competitionProperties.getMaxProjects();
    }

    public void addProjectToCompetition(String competitionId, ProjectEntity project) {
        Optional<CompetitionEntity> optionalCompetitionEntity = repository.findById(competitionId);

        if (optionalCompetitionEntity.isPresent()) {
            CompetitionEntity competitionEntity = optionalCompetitionEntity.get();

            // Competition competition = convertEntityToCompetition(competitionEntity);

            //if (!canAddProject(competition)) {
            //    throw new IllegalArgumentException("Cannot add more projects: limit reached");
            //}

            competitionEntity.getProjects().add(project);

            repository.save(competitionEntity);

            //return convertEntityToCompetition(competitionEntity);
        } else {
            throw new EntityNotFoundException("Competition not found with ID: " + competitionId);
        }
    }


    private CompetitionItemDto convertEntityToDto(CompetitionEntity entity) {
        CompetitionItemDto competitionDto = new CompetitionItemDto();
        competitionDto.setId(entity.getId());
        competitionDto.setName(entity.getName());
        competitionDto.setDescription(entity.getDescription());
        competitionDto.setVotingEndDate(entity.getVotingEndDate());
        competitionDto.setLogo(
            entity.getLogo() == null
                ? null
                : new ImageResponseDto(
                entity.getLogo().getUrl(),
                entity.getLogo().getPublicId(),
                entity.getLogo().getName()
            )
        );
        competitionDto.setProjects(entity.getProjects().stream().map(projectService::convertToDto).toList());
        competitionDto.setFinished(entity.getVotingEndDate().isBefore(Instant.now()) || entity.getVotingEndDate().equals(Instant.now()));
        competitionDto.setOrganizer(
            UserDto.builder()
                .id(entity.getOrganizer().getId())
                .email(entity.getOrganizer().getEmail())
                .fullName(entity.getOrganizer().getFullName())
                .logoUrl(entity.getOrganizer().getLogoUrl())
                .build()
        );

        return competitionDto;
    }

    @Override
    public void saveFromDto(CompetitionCreateDto competitionCreateDto, String userEmail) throws Exception {
        try {
            UserEntity competitionOrganizer = userService.findByEmail(userEmail).orElseThrow();
            CompetitionEntity competitionEntity = CompetitionEntity.builder()
                .name(competitionCreateDto.getName())
                .description(competitionCreateDto.getDescription())
                .votingEndDate(competitionCreateDto.getEndDate().toInstant(ZoneOffset.UTC))
                .organizer(competitionOrganizer)
                .build();
            System.out.println(competitionCreateDto.getEndDate().toInstant(ZoneOffset.UTC) + ": time)");
            if (competitionCreateDto.getLogo() != null && !competitionCreateDto.getLogo().isEmpty()) {
                saveImage(competitionEntity, competitionCreateDto.getLogo(), true);
            }

            super.save(competitionEntity);
            log.info("Project entity with id {} was successfully created.", competitionEntity.getId());
        } catch (Exception exception) {
            log.error("Error occurred while saving a project: {}", exception.getMessage());
            throw exception;
        }
    }

    public List<CompetitionItemDto> findAllAsDto() {
        Marker findMarker = MarkerManager.getMarker("COMPETITION_FIND");
        logger.info(findMarker, "Retrieving all competitions");

        List<CompetitionEntity> allCompetitionEntities = repository.findAll();

        return allCompetitionEntities.stream()
            .map(this::convertEntityToDto)
            .toList();
    }


    @Override
    public CompetitionItemDto findByIdAsDto(String id) {
        CompetitionEntity competitionEntity = repository.findById(id).orElseThrow();
        Marker findMarker = MarkerManager.getMarker("COMPETITION_FIND");
        logger.info(findMarker, "Searching for competition");

        return convertEntityToDto(competitionEntity);
    }

    @Override
    public boolean existsById(String id) {
        Optional<CompetitionEntity> competitionEntity = repository.findById(id);
        return competitionEntity.isPresent();
    }

    @Override
    @CacheEvict(value = "competitions", key = "#id")
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    private void saveImage(CompetitionEntity competition, MultipartFile image, boolean isMain) throws IOException {
        String publicUrl = cloudinaryService.upload(image, AppConstants.cloudinaryFolder);
        ImageEntity logo = ImageEntity.builder()
            .url(publicUrl)
            .main(isMain)
            .name(image.getOriginalFilename())
            .build();
        competition.addImage(logo);
    }
}
