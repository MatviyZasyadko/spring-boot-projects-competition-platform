package com.ukma.competition.platform.performance.trackers;

import com.ukma.competition.platform.votes.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceTrackerRecordRepository extends JpaRepository<PerformanceTrackerRecordEntity, String>, JpaSpecificationExecutor<PerformanceTrackerRecordEntity> {
}
