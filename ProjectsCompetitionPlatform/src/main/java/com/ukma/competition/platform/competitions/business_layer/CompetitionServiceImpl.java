package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.competitions.database_layer.CompetitionRepository;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.competitions.presentation_layer.CompetitionItemDto;
import com.ukma.competition.platform.projects.ProjectService;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Component
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompetitionServiceImpl extends GenericServiceImpl<CompetitionEntity, String, CompetitionRepository> implements CompetitionService {

    private static final Marker COMPETITION_MARKER = MarkerManager.getMarker("COMPETITION");
    CompetitionProperties competitionProperties;
    ProjectService projectService;

    @Autowired
    public CompetitionServiceImpl(
            CompetitionRepository repository,
            CompetitionProperties competitionProperties,
            ProjectService projectService
    ) {
        super(repository);
        this.competitionProperties = competitionProperties;
        this.projectService = projectService;
    }

    @Caching(evict = {
            @CacheEvict(value = "competitions", key = "#id"),
            @CacheEvict(value = "competitionsList", allEntries = true)
    })
    public Competition updateById(String id, Competition competition) {
        Optional<CompetitionEntity> optionalCompetitionEntity = repository.findById(id);

        Marker updateMarker = MarkerManager.getMarker("COMPETITION_UPDATE");
        logger.info(updateMarker, "Updating competition");

        if (optionalCompetitionEntity.isPresent()) {
            CompetitionEntity existingCompetitionEntity = optionalCompetitionEntity.get();
            CompetitionEntity updatedCompetitionEntity = repository.save(existingCompetitionEntity);

            logger.info(updateMarker, "Successfully updated competition with ID: {}", id);

            return null;
        } else {
            logger.error(updateMarker, "Failed to update competition with ID: {}. Not found.", id);
            ThreadContext.clearAll();
            throw new EntityNotFoundException("Competition not found with ID: " + id);
        }
    }

    public boolean canAddProject(Competition competition) {
        int currentProjects = competition.getProjects().size();
        return currentProjects < competitionProperties.getMaxProjects();
    }

    // public Competition addProjectToCompetition(String competitionId, ProjectEntity project) {
    //     Optional<CompetitionEntity> optionalCompetitionEntity = repository.findById(competitionId);
//
    //     if (optionalCompetitionEntity.isPresent()) {
    //         CompetitionEntity competitionEntity = optionalCompetitionEntity.get();
//
    //         Competition competition = convertEntityToCompetition(competitionEntity);
//
    //         if (!canAddProject(competition)) {
    //             throw new IllegalArgumentException("Cannot add more projects: limit reached");
    //         }
//
    //         competitionEntity.getProjects().add(project);
//
    //         repository.save(competitionEntity);
//
    //         return convertEntityToCompetition(competitionEntity);
    //     } else {
    //         throw new EntityNotFoundException("Competition not found with ID: " + competitionId);
    //     }
    // }


    private CompetitionItemDto convertEntityToDto(CompetitionEntity entity) {
        CompetitionItemDto competition = new CompetitionItemDto();
        competition.setId(entity.getId());
        competition.setName(entity.getName());
        competition.setDescription(entity.getDescription());
        competition.setBeginDate(entity.getBeginDate());
        competition.setVotingBeginDate(entity.getVotingBeginDate());
        competition.setVotingEndDate(entity.getVotingEndDate());
        competition.setImages(entity.getImages());
        competition.setProjects(entity.getProjects().stream().map(projectService::convertToDto).toList());
        competition.setCreator(entity.getCreator());

        return competition;
    }

    private Optional<Competition> convertEntityToDto(Optional<CompetitionEntity> entity) {

        return entity.isEmpty() ? Optional.empty() : convertEntityToDto(entity);
    }

    private CompetitionEntity convertCompetitionToEntity(Competition competition) {
        return CompetitionEntity.builder()
                .name(competition.getName())
                .description(competition.getDescription())
                .beginDate(competition.getBeginDate())
                .votingBeginDate(competition.getVotingBeginDate())
                .votingEndDate(competition.getVotingEndDate())
                .hasPrizePool(competition.getHasPrizePool())
                .priceDescription(competition.getPriceDescription())
                .prizePool(competition.getPrizePool())
                .images(competition.getImages())
                .projects(competition.getProjects())
                .tags(competition.getTags())
                .payments(competition.getPayments())
                .creator(competition.getCreator())
                .build();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "competitions", key = "#competition.id"),
            @CacheEvict(value = "competitionsList", allEntries = true)
    })
    public Competition save(Competition competition) {
        CompetitionEntity competitionEntity = convertCompetitionToEntity(competition);
        CompetitionEntity savedEntity = repository.saveAndFlush(competitionEntity);
        return null;
    }

    @Cacheable("competitionsList")
    public List<CompetitionItemDto> findAllAsDto() {
        Marker findMarker = MarkerManager.getMarker("COMPETITION_FIND");
        logger.info(findMarker, "Retrieving all competitions");

        List<CompetitionEntity> allCompetitionEntities = repository.findAll();

        return allCompetitionEntities.stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Cacheable(value = "competitions", key = "#id")
    public Optional<Competition> findByIdAsDto(String id) {
        Optional<CompetitionEntity> competitionEntity = repository.findById(id);

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
}
