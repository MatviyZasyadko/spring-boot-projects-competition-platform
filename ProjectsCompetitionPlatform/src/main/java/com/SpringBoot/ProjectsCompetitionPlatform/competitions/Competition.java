package com.SpringBoot.ProjectsCompetitionPlatform.competitions;


import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
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
}
