package com.SpringBoot.ProjectsCompetitionPlatform.users;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.votes.Vote;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
public class User{

    private String firstName, lastName;
    @Setter
    @Getter
    private String status = "User";

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName, String status){
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    public Project createProject(String name, Competition competition, User... users){
        this.setStatus("Developer");
        for(User u : users){
            u.setStatus("Developer");
        }

        List<User> allUsers = new ArrayList<>(List.of(users));
        allUsers.add(this);

        Project newProject = new Project(name, allUsers, competition);
        competition.addProject(newProject);
        return newProject;
    }

    public Competition createCompetition(String name, String description, Date beginDate, Integer prizePool){
        return new Competition(name, description, beginDate, prizePool, this);
    }

    public Vote makeVote(Project project){
        return new Vote(project, this, new Date());
    }

    public String getName(){
        return firstName + " " + lastName;
    }

    public String toString(){
        return firstName + " " + lastName + ", Status: " + status;
    }


}