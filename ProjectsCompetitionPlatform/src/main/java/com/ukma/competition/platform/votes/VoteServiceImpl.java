package com.ukma.competition.platform.votes;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl extends GenericServiceImpl<Vote, String, VoteRepository> implements VoteService {

    @Autowired
    public VoteServiceImpl(VoteRepository repository) {
        super(repository);
    }
}
