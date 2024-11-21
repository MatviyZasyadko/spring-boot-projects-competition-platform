package com.ukma.competition.platform.scheduled.tasks;

import com.ukma.competition.platform.reports.ReportEntity;
import com.ukma.competition.platform.reports.ReportService;
import com.ukma.competition.platform.reports.ReportStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ReportRemoveScheduledTaskImpl implements ReportRemoveScheduledTask {

    ReportService reportService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void removeOldApprovedReports() {
        for (ReportEntity report : reportService.findAllByStatus(ReportStatus.APPROVED)) {
            if (report.getApproveDate() == null) {
                log.warn(
                    "During execution of a scheduled task, report ({}) with approved status, but without approve date was found",
                    report.getId()
                );
                continue;
            }
            if (report.getApproveDate().isBefore(Instant.now().minus(30, ChronoUnit.DAYS))) {
                reportService.deleteById(report.getId());
            }
        }
    }

}
