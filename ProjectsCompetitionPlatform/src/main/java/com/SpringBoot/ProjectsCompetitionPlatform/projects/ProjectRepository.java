package com.SpringBoot.ProjectsCompetitionPlatform.projects;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectRepository {

    Competition competitionFirst = new Competition(
        "competition 1",
        "wow competition!",
        new ArrayList<>(),
        new Date(),
        342
    );

    Competition competitionSecond = new Competition(
        "competition 2",
        "wow competition 2!",
        new ArrayList<>(),
        new Date(),
        22323
    );

    List<Project> projects = new ArrayList<>(List.of(
        new Project(
            "project1",
            new ArrayList<>(),
            competitionFirst
        ),
        new Project(
            "project2",
            new ArrayList<>(),
            competitionFirst
        ),
        new Project(
            "project3",
            new ArrayList<>(),
            competitionSecond
        ),
        new Project(
            "project4",
            new ArrayList<>(),
            competitionSecond
        )
    ));

    public List<Project> getAll() {
        return projects;
    }

    public Project getByName(String name) {
        return projects.stream()
            .filter(project -> project.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    public void deleteByName(String name) {
        this.projects = this.projects.stream()
            .filter(project -> !project.getName().equalsIgnoreCase(name))
            .toList();
    }
}
