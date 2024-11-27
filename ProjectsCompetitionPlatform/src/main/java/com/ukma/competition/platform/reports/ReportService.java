package com.ukma.competition.platform.reports;

import com.ukma.competition.platform.reports.dto.ReportCreateDto;
import com.ukma.competition.platform.shared.GenericService;
import com.ukma.competition.platform.users.UserEntity;

import java.util.List;

public interface ReportService extends GenericService<ReportEntity, String> {

    List<ReportEntity> findAllByStatus(ReportStatus reportStatus);

    List<ReportEntity> findAllByUser(UserEntity user);

    void saveFromDto(ReportCreateDto reportCreateDto, String userEmail) throws Exception;
}
