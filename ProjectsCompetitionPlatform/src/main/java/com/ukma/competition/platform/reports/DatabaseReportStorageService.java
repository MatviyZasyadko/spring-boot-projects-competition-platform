package com.ukma.competition.platform.reports;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
@Service
@ConditionalOnClass(name = "com.example.database.DatabaseService")
public class DatabaseReportStorageService implements ReportStorageService {

    private final ReportRepository reportRepository;
    public DatabaseReportStorageService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public void storeReport(Report report) {
        if (report.getReportStatus() == ReportStatus.APPROVED) {
            reportRepository.save(report);
            System.out.println("Звіт збережено: " + report.getTopic());
        } else {
            System.out.println("Звіт не збережено, оскільки статус: " + report.getReportStatus());
        }
    }
}