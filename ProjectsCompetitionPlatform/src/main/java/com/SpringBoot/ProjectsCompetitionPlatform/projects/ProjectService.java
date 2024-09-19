package com.SpringBoot.ProjectsCompetitionPlatform.projects;

import java.util.List;

public interface ProjectService {

    boolean create(Project project);
    List<Project> getAllByCompetitionName(String competitionName);
    Project getByName(String name);
    void deleteByName(String name);
}
