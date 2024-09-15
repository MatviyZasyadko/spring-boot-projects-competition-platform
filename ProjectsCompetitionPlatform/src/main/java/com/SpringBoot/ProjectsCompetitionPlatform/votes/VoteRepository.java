package com.SpringBoot.ProjectsCompetitionPlatform.votes;

import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.users.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteRepository {

    List<Vote> votes = new ArrayList<>(List.of(
        new Vote(new Project(), new User(), new Date()),
        new Vote(new Project(), new User(), new Date()),
        new Vote(new Project(), new User(), new Date()),
        new Vote(new Project(), new User(), new Date())
    ));

    public void create(Vote vote) {
        votes.add(vote);
    }
    public List<Vote> getAll() {
        return votes;
    }

    public Vote getOneByUsername(String name) {
        return votes.stream()
            .filter(vote -> vote.getUser().getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    public boolean delete(Vote vote) {
        return votes.remove(vote);
    }

}
