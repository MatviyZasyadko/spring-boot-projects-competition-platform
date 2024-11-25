package com.ukma.competition.platform.reports;

import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.votes.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, String>, JpaSpecificationExecutor<ReportEntity> {

    List<ReportEntity> findAllByReportStatus(ReportStatus reportStatus);
    List<ReportEntity> findAllByUser(UserEntity user);
}
