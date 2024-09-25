package com.SpringBoot.ProjectsCompetitionPlatform.projects;

import java.util.List;

public interface ProjectService {

    void save(Project project);
    List<Project> findAll();
    Project findOneById(String id);
    void deleteById(String name);
}
