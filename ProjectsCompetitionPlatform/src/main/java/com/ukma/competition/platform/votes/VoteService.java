package com.ukma.competition.platform.votes;

import com.ukma.competition.platform.shared.GenericService;

import java.util.List;

public interface VoteService extends GenericService<Vote, String> {
    List<Object[]> findTop5ProjectsByVoteCount(int topCount);
}
