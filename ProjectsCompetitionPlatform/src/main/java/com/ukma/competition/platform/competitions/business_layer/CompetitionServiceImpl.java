package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.competitions.database_layer.CompetitionRepository;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.competitions.presentation_layer.CompetitionItemDto;
import com.ukma.competition.platform.competitions.presentation_layer.ProjectApplyToCompetitionDto;
import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.projects.ProjectService;
import com.ukma.competition.platform.projects.dto.ProjectListItemDto;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.shared.constants.AppConstants;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import com.ukma.competition.platform.users.dto.UserDto;
import com.ukma.competition.platform.votes.VoteEntity;
import com.ukma.competition.platform.votes.VoteService;
import com.ukma.competition.platform.votes.dto.VoteDto;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
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
    CompetitionProperties competitionProperties;
    ProjectService projectService;
    UserService userService;
    CloudinaryService cloudinaryService;
    VoteService voteService;

    @Autowired
    public CompetitionServiceImpl(
        CompetitionRepository repository,
        CompetitionProperties competitionProperties,
        ProjectService projectService,
        UserService userService,
        CloudinaryService cloudinaryService,
        VoteService voteService
    ) {
        super(repository);
        this.competitionProperties = competitionProperties;
        this.projectService = projectService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.voteService = voteService;
    }

    public boolean canAddProject(Competition competition) {
        int currentProjects = competition.getProjects().size();
        return currentProjects < competitionProperties.getMaxProjects();
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
        competitionDto.setProjects(entity.getProjects()
            .stream()
            .map(projectService::convertToDto)
            .map(projectDto -> countVotes(entity, projectDto))
            .sorted(
                (first, second) -> Integer.compare(second.getVotesAmount(), first.getVotesAmount())
            )
            .toList()
        );
        competitionDto.setFinished(entity.getVotingEndDate().isBefore(Instant.now()) || entity.getVotingEndDate().equals(Instant.now()));
        competitionDto.setOrganizer(
            UserDto.builder()
                .id(entity.getOrganizer().getId())
                .email(entity.getOrganizer().getEmail())
                .fullName(entity.getOrganizer().getFullName())
                .logoUrl(entity.getOrganizer().getLogoUrl())
                .build()
        );
        competitionDto.setTotalVotesAmount((double) entity.getVotes().size());

        return competitionDto;
    }

    private ProjectListItemDto countVotes(CompetitionEntity competition, ProjectListItemDto projectDto) {
        projectDto.setVotesAmount(
            (int) competition.getVotes().stream().filter(vote -> vote.getProject().getId().equals(projectDto.getId())).count()
        );

        return projectDto;
    }

    @Override
    public void saveFromDto(CompetitionCreateDto competitionCreateDto, String userEmail) throws IOException {
        try {
            UserEntity competitionOrganizer = userService.findByEmail(userEmail).orElseThrow();
            CompetitionEntity competitionEntity = CompetitionEntity.builder()
                .name(competitionCreateDto.getName())
                .description(competitionCreateDto.getDescription())
                .votingEndDate(competitionCreateDto.getEndDate().toInstant(ZoneOffset.UTC))
                .organizer(competitionOrganizer)
                .build();

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

    public void applyProjectToCompetition(ProjectApplyToCompetitionDto projectApplyToCompetitionDto, String competitionId) {
        if (StringUtils.isBlank(projectApplyToCompetitionDto.getProjectId())) {
            throw new IllegalArgumentException("You should provide project with correct id!");
        }

        CompetitionEntity competition = super.findById(competitionId).orElseThrow();
        ProjectEntity project = this.projectService.findById(projectApplyToCompetitionDto.getProjectId()).orElseThrow();

        if (competition.getProjects().contains(project)) {
            throw new IllegalArgumentException("This project is already participating in this competition!");
        }

        competition.addProject(project);
        super.save(competition);
    }

    public boolean addOrChangeVote(CompetitionEntity competition, ProjectEntity project, UserEntity user) {
        VoteEntity voteEntity = VoteEntity.builder()
            .competition(competition)
            .project(project)
            .user(user)
            .build();

        List<VoteEntity> alreadyExistingVotes = voteService.findByUserAndCompetition(user, competition);
        for (VoteEntity alreadyExistingVote : alreadyExistingVotes) {
            voteService.deleteById(alreadyExistingVote.getId());
        }

        voteService.save(voteEntity);
        return true;
    }

    public String findProjectIdWithVoteFromUser(String competitionId, UserEntity user) {
        CompetitionEntity competitionEntity = findById(competitionId)
            .orElseThrow(() -> new IllegalArgumentException("Competition not found"));

        var votes = competitionEntity.getVotes();

        for (VoteEntity vote : votes) {
            if (vote.getUser().equals(user)) {
                return vote.getProject().getId();
            }
        }

        return null;
    }

    public List<CompetitionItemDto> findAllByOrganizer(UserEntity user) {
        List<CompetitionEntity> competitionEntities = repository.findAllByOrganizer(user);
        return competitionEntities.stream().map(this::convertEntityToDto).toList();
    }

    public List<CompetitionItemDto> findAllAsDto() {
        Marker findMarker = MarkerManager.getMarker("COMPETITION_FIND");
        logger.info(findMarker, "Retrieving all competitions");

        List<CompetitionEntity> allCompetitionEntities = repository.findAll();

        return allCompetitionEntities.stream()
            .map(this::convertEntityToDto)
            .toList();
    }

    public void finishCompetition(String id) {
        super.findById(id).orElseThrow().setVotingEndDate(Instant.now());
    }

    @Override
    public CompetitionItemDto findByIdAsDto(String id, String username) {
        CompetitionEntity competitionEntity = repository.findById(id).orElseThrow();
        UserEntity userEntity = this.userService.findByEmail(username).orElseThrow();
        VoteEntity voteEntity = competitionEntity.getVotes()
            .stream()
            .filter(vote -> vote.getUser().getId().equals(userEntity.getId()))
            .findFirst()
            .orElse(null);
        VoteDto voteDto = voteEntity == null
            ? null
            : new VoteDto(
                new UserDto(
                    userEntity.getId(),
                    userEntity.getFullName(),
                    userEntity.getEmail(),
                    userEntity.getLogoUrl()
                ),
                voteEntity.getProject().getId()
            );


        Marker findMarker = MarkerManager.getMarker("COMPETITION_FIND");
        logger.info(findMarker, "Searching for competition");

        CompetitionItemDto competitionItemDto = convertEntityToDto(competitionEntity);
        competitionItemDto.setVoteDto(
            voteDto
        );

        return competitionItemDto;
    }

    @Override
    public boolean existsById(String id) {
        Optional<CompetitionEntity> competitionEntity = repository.findById(id);
        return competitionEntity.isPresent();
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
