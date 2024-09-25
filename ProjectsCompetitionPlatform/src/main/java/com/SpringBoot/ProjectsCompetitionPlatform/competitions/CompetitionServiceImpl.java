package com.SpringBoot.ProjectsCompetitionPlatform.competitions;

import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    CompetitionRepository competitionRepository;
    ProjectService projectService;

    @Override
    @Transactional
    public void save(Competition competition) {
        competitionRepository.save(competition);
    }

    @Transactional
    public void addProject(Project newProject, Competition competition) {
        boolean projectAlreadyExistsInCompetition = competition.getProjects().stream()
            .anyMatch(project -> project.getId().equals(newProject.getId()));
        if (projectAlreadyExistsInCompetition) {
            throw new IllegalArgumentException("Project already exists in this competition");
        } else {
            competition.getProjects().add(newProject);
            newProject.getCompetitions().add(competition);
            competitionRepository.save(competition);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Competition> findAll() {
        return competitionRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<Competition> findAll(CompetitionFilter filter) {
        Specification<Competition> specification = buildFilter(filter);
        return competitionRepository.findAll(specification);
    }

    private Specification<Competition> buildFilter(CompetitionFilter filter) {
        if (filter == null) {
            return Specification.anyOf();
        }
        List<Specification<Competition>> specs = new ArrayList<>();

        if (filter.getName() != null) {
            specs.add((root, query, builder) -> builder.like(root.get("name"), filter.getName()));
        }
        if (filter.getDescription() != null) {
            specs.add((root, query, builder) -> builder.like(root.get("description"), filter.getDescription()));
        }

        return Specification.allOf(specs);
    }

    @Transactional(readOnly = true)
    public Competition findOneByName(String name) {
        return competitionRepository.findByName(name).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Competition findOneById(String id) {
        return competitionRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Competition updateOneById(String id, Competition competition) {
        Competition competitionToUpdate = this.findOneById(competition.getId());

        competitionToUpdate.setName(competition.getName());
        competitionToUpdate.setDescription(competition.getDescription());
        competitionToUpdate.setPrizePool(competition.getPrizePool());
        competitionToUpdate.setBeginDate(competition.getBeginDate());

        this.save(competitionToUpdate);

        return competitionToUpdate;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        competitionRepository.deleteById(id);
    }
}
