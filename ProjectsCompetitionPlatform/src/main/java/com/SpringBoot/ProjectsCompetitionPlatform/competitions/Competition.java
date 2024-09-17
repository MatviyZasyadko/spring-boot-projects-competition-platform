package com.SpringBoot.ProjectsCompetitionPlatform.competitions;


import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.users.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class Competition {

    String name;
    String description;
    List<Project> projects;
    Date beginDate;
    Integer prizePool;

    public Competition(String name, String description, Date beginDate, Integer prizePool){
        this.name = name;
        this.description = description;
        this.beginDate = beginDate;
        this.prizePool = prizePool;
    }

    public void addProject(Project project){
        projects.add(project);
    }

}
