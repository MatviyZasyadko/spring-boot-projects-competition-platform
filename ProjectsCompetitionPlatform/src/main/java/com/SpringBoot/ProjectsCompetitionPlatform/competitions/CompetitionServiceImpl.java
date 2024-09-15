package com.SpringBoot.ProjectsCompetitionPlatform.competitions;

import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.ProjectService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompetitionServiceImpl implements CompetitionService {

    @Autowired
    CompetitionRepository competitionRepository;

    @Autowired
    ProjectService projectService;

    @Override
    public boolean create(Competition competition) {
        if (competition.getProjects().isEmpty()) {
            List<Project> competitionProjects = projectService.getAllByCompetitionName(competition.getName());
            competition.setProjects(competitionProjects);
        }
        return competitionRepository.create(competition);
    }

    @Override
    public List<Competition> getAll() {
        return competitionRepository.getAll();
    }

    @Override
    public Competition getOneByName(String name) {
        return competitionRepository.getOneByName(name);
    }

    @Override
    public Competition updateByName(String name, Competition competition) {
        return competitionRepository.updateByName(competition, name);
    }

    @Override
    public boolean deleteByName(String name) {
        return competitionRepository.deleteByName(name);
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
