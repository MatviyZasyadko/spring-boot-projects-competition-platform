package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.competitions.database_layer.CompetitionRepository;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.projects.Project;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompetitionServiceImpl implements CompetitionService {

    CompetitionRepository competitionRepository;
    CompetitionProperties competitionProperties;

    @Autowired
    public CompetitionServiceImpl(CompetitionRepository repository, CompetitionProperties competitionProperties) {
        this.competitionRepository = repository;
        this.competitionProperties = competitionProperties;
    }
    public Competition updateById(String id, Competition competition) {
        Optional<CompetitionEntity> optionalCompetitionEntity = competitionRepository.findById(id);

        if (optionalCompetitionEntity.isPresent()) {
            CompetitionEntity existingCompetitionEntity = optionalCompetitionEntity.get();

            existingCompetitionEntity.setName(competition.getName());
            existingCompetitionEntity.setDescription(competition.getDescription());
            existingCompetitionEntity.setBeginDate(competition.getBeginDate());
            existingCompetitionEntity.setVotingBeginDate(competition.getVotingBeginDate());
            existingCompetitionEntity.setVotingEndDate(competition.getVotingEndDate());
            existingCompetitionEntity.setHasPrizePool(competition.getHasPrizePool());
            existingCompetitionEntity.setPriceDescription(competition.getPriceDescription());
            existingCompetitionEntity.setPrizePool(competition.getPrizePool());
            existingCompetitionEntity.setImages(competition.getImages());
            existingCompetitionEntity.setProjects(competition.getProjects());
            existingCompetitionEntity.setTags(competition.getTags());
            existingCompetitionEntity.setPayments(competition.getPayments());

            CompetitionEntity updatedCompetitionEntity = competitionRepository.save(existingCompetitionEntity);
            return convertEntityToCompetition(updatedCompetitionEntity);
        } else {
            throw new EntityNotFoundException("Competition not found with ID: " + id);
        }
    }

    public boolean canAddProject(Competition competition) {
        int currentProjects = competition.getProjects().size();
        return currentProjects < competitionProperties.getMaxProjects();
    }

    public Competition addProjectToCompetition(String competitionId, Project project) {
        Optional<CompetitionEntity> optionalCompetitionEntity = competitionRepository.findById(competitionId);

        if (optionalCompetitionEntity.isPresent()) {
            CompetitionEntity competitionEntity = optionalCompetitionEntity.get();

            Competition competition = convertEntityToCompetition(competitionEntity);

            if (!canAddProject(competition)) {
                throw new IllegalArgumentException("Cannot add more projects: limit reached");
            }

            competitionEntity.getProjects().add(project);

            competitionRepository.save(competitionEntity);

            return convertEntityToCompetition(competitionEntity);
        } else {
            throw new EntityNotFoundException("Competition not found with ID: " + competitionId);
        }
    }



    private Competition convertEntityToCompetition(CompetitionEntity entity) {
        Competition competition = new Competition();
        competition.setId(entity.getId());
        competition.setName(entity.getName());
        competition.setDescription(entity.getDescription());
        competition.setBeginDate(entity.getBeginDate());
        competition.setVotingBeginDate(entity.getVotingBeginDate());
        competition.setVotingEndDate(entity.getVotingEndDate());
        competition.setHasPrizePool(entity.getHasPrizePool());
        competition.setPriceDescription(entity.getPriceDescription());
        competition.setPrizePool(entity.getPrizePool());
        competition.setImages(entity.getImages());
        competition.setProjects(entity.getProjects());
        competition.setTags(entity.getTags());
        competition.setPayments(entity.getPayments());
        return competition;
    }

    private Optional<Competition> convertEntityToCompetition(Optional<CompetitionEntity> entity) {

        return entity.isEmpty() ? Optional.empty() : convertEntityToCompetition(entity);
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
                .build();
    }

    @Override
    public Competition save(Competition competition) {
        CompetitionEntity competitionEntity = convertCompetitionToEntity(competition);
        CompetitionEntity savedEntity = competitionRepository.saveAndFlush(competitionEntity);
        return convertEntityToCompetition(savedEntity);
    }

    @Override
    public List<Competition> findAll() {
        List<CompetitionEntity> allCompetitionEntities = competitionRepository.findAll();
        return allCompetitionEntities.stream()
                .map(this::convertEntityToCompetition)
                .toList();
    }

    @Override
    public Optional<Competition> findById(String id) {
        Optional<CompetitionEntity> competitionEntity = competitionRepository.findById(id);
        return convertEntityToCompetition(competitionEntity);
    }

    @Override
    public boolean existsById(String id) {
        Optional<CompetitionEntity> competitionEntity = competitionRepository.findById(id);
        return competitionEntity.isPresent();
    }

    @Override
    public void deleteById(String id) {
        competitionRepository.deleteById(id);
    }
}
