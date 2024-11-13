package com.ukma.competition.platform.votes;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoteServiceImpl extends GenericServiceImpl<Vote, String, VoteRepository> implements VoteService {
    @Autowired
    public VoteServiceImpl(VoteRepository repository) {
        super(repository);
    }

    public List<Object[]> findTop5ProjectsByVoteCount(int topCount) {
        if (topCount < 0) {
            return new ArrayList<>();
        }
        return repository.findTop5ProjectsByVoteCount(PageRequest.of(0, topCount));
    }
}
