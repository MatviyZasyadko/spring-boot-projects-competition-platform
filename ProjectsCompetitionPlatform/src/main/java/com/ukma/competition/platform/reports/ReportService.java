package com.ukma.competition.platform.reports;

import com.ukma.competition.platform.shared.GenericService;

import java.util.List;

public interface ReportService extends GenericService<ReportEntity, String> {

    List<ReportEntity> findAllByStatus(ReportStatus reportStatus);
}
