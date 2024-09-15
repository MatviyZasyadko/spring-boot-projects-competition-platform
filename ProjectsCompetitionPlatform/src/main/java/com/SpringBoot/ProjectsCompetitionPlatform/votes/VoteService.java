package com.SpringBoot.ProjectsCompetitionPlatform.votes;

import java.util.List;

public interface VoteService {

    void create(Vote vote);
    List<Vote> getAll();
    Vote getOneByUser(String name);
    boolean delete(Vote vote);
}
