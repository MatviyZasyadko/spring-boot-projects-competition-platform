package com.ukma.competition.platform.votes;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.users.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, String>, JpaSpecificationExecutor<VoteEntity> {

    @Query("SELECT v.project, COUNT(v) AS voteCount " +
            "FROM VoteEntity v " +
            "GROUP BY v.project " +
            "ORDER BY voteCount DESC")
    List<Object[]> findTop5ProjectsByVoteCount(Pageable pageable);


    List<VoteEntity> findByUserAndCompetition(UserEntity user, CompetitionEntity competition);
}
