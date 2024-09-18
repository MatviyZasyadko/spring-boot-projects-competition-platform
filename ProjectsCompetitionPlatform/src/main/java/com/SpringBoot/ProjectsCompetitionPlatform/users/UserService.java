package com.SpringBoot.ProjectsCompetitionPlatform.users;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.votes.Vote;

import java.util.Date;
import java.util.List;

public interface UserService {

    public void createUser(String firstName, String lastName);

    public List<User> getAll();

    public User getUserByName(String firstName, String lastName);

    public boolean deleteUser(User user);

    public Project createProject(String name, Competition competition, User... users);

    public Competition createCompetition(String name, String description, Date beginDate, Integer prizePool, User creator);

    public Vote makeVote(Project project, User user);

}
