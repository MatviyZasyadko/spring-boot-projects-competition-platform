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

    public String getName(){
        return firstName + " " + lastName;
    }

    public String toString(){
        return firstName + " " + lastName + ", Status: " + status;
    }


}