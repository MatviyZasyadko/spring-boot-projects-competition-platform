package com.ukma.competition.platform.votes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, String>, JpaSpecificationExecutor<Vote> {

    @Query("SELECT v.project, COUNT(v) AS voteCount " +
            "FROM Vote v " +
            "GROUP BY v.project " +
            "ORDER BY voteCount DESC")
    List<Object[]> findTop5ProjectsByVoteCount(Pageable pageable);
}