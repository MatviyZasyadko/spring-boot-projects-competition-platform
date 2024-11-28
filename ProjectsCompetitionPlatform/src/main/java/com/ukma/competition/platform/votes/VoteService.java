package com.ukma.competition.platform.votes;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.shared.GenericService;
import com.ukma.competition.platform.users.UserEntity;

import java.util.List;

public interface VoteService extends GenericService<VoteEntity, String> {
    List<Object[]> findTop5ProjectsByVoteCount(int topCount);
    List<VoteEntity> findByUserAndCompetition(UserEntity user, CompetitionEntity competition);
}
