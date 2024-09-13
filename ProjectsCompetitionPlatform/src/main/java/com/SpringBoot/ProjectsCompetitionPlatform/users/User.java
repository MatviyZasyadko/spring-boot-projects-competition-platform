package com.SpringBoot.ProjectsCompetitionPlatform.users;


public class User{

    private String firstName, lastName;

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getName(){
        return firstName + " " + lastName;
    }

    public String toString(){
        return firstName + " " + lastName;
    }


}