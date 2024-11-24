package com.ukma.competition.platform.reports;

import com.ukma.competition.platform.projects.dto.ProjectCreateDto;
import com.ukma.competition.platform.reports.dto.ReportCreateDto;
import com.ukma.competition.platform.shared.GenericService;

import java.util.List;

public interface ReportService extends GenericService<ReportEntity, String> {

    List<ReportEntity> findAllByStatus(ReportStatus reportStatus);
    void saveFromDto(ReportCreateDto reportCreateDto, String userEmail) throws Exception;
}
