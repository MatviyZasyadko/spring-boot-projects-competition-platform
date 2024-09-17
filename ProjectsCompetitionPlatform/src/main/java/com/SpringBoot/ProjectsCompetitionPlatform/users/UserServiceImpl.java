package com.SpringBoot.ProjectsCompetitionPlatform.users;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.votes.Vote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements UserService{


    @Override
    public Project createProject(String name, Competition competition, User... users) {
        for(User u : users){
            u.setStatus("Developer");
        }

        List<User> allUsers = new ArrayList<>(List.of(users));

        Project newProject = new Project(name, allUsers, competition);
        competition.addProject(newProject);
        return newProject;
    }

    @Override
    public Competition createCompetition(String name, String description, Date beginDate, Integer prizePool, User creator) {
        return new Competition(name, description, beginDate, prizePool, creator);
    }


    @Override
    public Vote makeVote(Project project, User user) {
        return new Vote(project, user, new Date());
    }
}
