package com.ukma.competition.platform.reports;

import com.ukma.competition.platform.reports.dto.ReportCreateDto;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.users.User;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ReportServiceImpl extends GenericServiceImpl<ReportEntity, String, ReportRepository> implements ReportService {

    UserService userService;

    @Autowired
    public ReportServiceImpl(ReportRepository repository, UserService userService) {
        super(repository);
        this.userService = userService;
    }

    public List<ReportEntity> findAllByStatus(ReportStatus reportStatus) {
        return repository.findAllByReportStatus(reportStatus);
    }

    public List<ReportEntity> findAllByUser(UserEntity user) {
        return repository.findAllByUser(user);
    }

    @Transactional
    public void saveFromDto(ReportCreateDto reportCreateDto, String userEmail) {
        try {
            UserEntity reportCreator = userService.findByEmail(userEmail).orElseThrow();
            ReportEntity report = ReportEntity.builder()
                .description(reportCreateDto.getDescription())
                .topic(reportCreateDto.getTopic())
                .approveDate(null)
                .user(reportCreator)
                .reportStatus(ReportStatus.IN_PROCESS)
                .build();

            super.save(report);
            log.info("Report entity with id {} was successfully created.", report.getId());
        } catch (Exception exception) {
            log.error("Error occurred while saving a report: {}", exception.getMessage());
            throw exception;
        }
    }
}
