package com.SpringBoot.ProjectsCompetitionPlatform.projects;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import com.SpringBoot.ProjectsCompetitionPlatform.competitions.CompetitionService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    CompetitionService competitionService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, CompetitionService competitionService) {
        this.projectRepository = projectRepository;
        this.competitionService = competitionService;
    }

    @Override
    public boolean create(Project project) {
        Competition competition = competitionService.getOneByName(project.getCompetition().getName());
        if (competition != null) {
            competition.addProject(project);
            return projectRepository.create(project);
        }
        return false;
    }

    @Override
    public List<Project> getAllByCompetitionName(String competitionName) {
        return projectRepository.getAll()
            .stream()
            .filter(getAllByCompetitionNamePredicate(competitionName))
            .toList();
    }

    private Predicate<Project> getAllByCompetitionNamePredicate(String competitionName) {
        return project -> {
            if (competitionName == null) {
                return true;
            } else {
                return project.getCompetition().getName().equals(competitionName);
            }
        };
    }

    @Override
    public Project getByName(String name) {
        return projectRepository.getByName(name);
    }

    @Override
    public void deleteByName(String name) {
        projectRepository.deleteByName(name);
    }

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
}
