package com.SpringBoot.ProjectsCompetitionPlatform.users;

import com.SpringBoot.ProjectsCompetitionPlatform.votes.Vote;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    List<User> allUsers = new ArrayList<>();

    public void createUser(User user){
        allUsers.add(user);
    }

    public List<User> getAll(){
        return allUsers;
    }

    public User getUserByName(String firstName, String lastName){
        String fullName = firstName + " " + lastName;
        return allUsers.stream()
                .filter(user -> user.getName().equals(fullName))
                .findFirst().orElse(null);
    }


    public boolean deleteUser(User user){
        return allUsers.remove(user);
    }

}
