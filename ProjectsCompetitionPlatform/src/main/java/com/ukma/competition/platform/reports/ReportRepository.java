package com.ukma.competition.platform.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, String> {

    List<ReportEntity> findAllByReportStatus(ReportStatus reportStatus);
}
