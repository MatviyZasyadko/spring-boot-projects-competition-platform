package com.SpringBoot.ProjectsCompetitionPlatform.users;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import com.SpringBoot.ProjectsCompetitionPlatform.competitions.CompetitionRepository;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.votes.Vote;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public void createUser(String firstName, String lastName){
        String fullName = firstName + " " + lastName;
        User newUser = new User(firstName, lastName);
        User user = userRepository.getUserByName(firstName, lastName);
        if(user == null){
            userRepository.createUser(newUser);
        }
        else {
            throw new IllegalArgumentException("User with this name already exists");
        }
    }

    @Override
    public User getUserByName(String firstName, String lastName){
        return userRepository.getUserByName(firstName, lastName);
    }

    @Override
    public List<User> getAll(){
        return userRepository.getAll();
    }

    @Override
    public boolean deleteUser(User user){
        return userRepository.deleteUser(user);
    }

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
        return new Competition(name, description, beginDate, prizePool);
    }


    @Override
    public Vote makeVote(Project project, User user) {
        return new Vote(project, user, new Date());
    }
}
