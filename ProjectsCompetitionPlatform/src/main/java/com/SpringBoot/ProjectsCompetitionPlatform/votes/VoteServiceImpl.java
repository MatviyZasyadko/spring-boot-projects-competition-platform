package com.SpringBoot.ProjectsCompetitionPlatform.votes;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteServiceImpl implements VoteService {

    @Autowired
    VoteRepository voteRepository;

    @Override
    public void create(Vote vote) {

    }

    private Predicate<Vote> createPredicate(Vote vote) {
        return checkVote -> checkVote.getUser().getName().equalsIgnoreCase(vote.getUser().getName())
                            && checkVote.getProject().getName().equalsIgnoreCase(vote.getProject().getName());
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.getAll();
    }

    @Override
    public Vote getOneByUser(String username) {
        return voteRepository.getOneByUsername(username);
    }

    @Override
    public boolean delete(Vote vote) {
        return voteRepository.delete(vote);
    }


}
