package com.SpringBoot.ProjectsCompetitionPlatform.projects;

import com.SpringBoot.ProjectsCompetitionPlatform.users.User;

public class Project {

    private String name;
    private User[] team;

    public Project(String name, User... team){
        this.name = name;
        this.team = team;
    }






}
