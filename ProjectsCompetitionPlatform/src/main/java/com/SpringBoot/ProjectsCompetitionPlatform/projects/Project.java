package com.SpringBoot.ProjectsCompetitionPlatform.projects;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import com.SpringBoot.ProjectsCompetitionPlatform.users.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    String name = "default name";
    List<User> participants;
    Competition competition;

    @Override
    public String toString() {
        return "Project(" +
               "name='" + name + '\'' +
               ", participants=" + participants +
               ", competition=" + competition.getName() +
               ')';
    }
}
