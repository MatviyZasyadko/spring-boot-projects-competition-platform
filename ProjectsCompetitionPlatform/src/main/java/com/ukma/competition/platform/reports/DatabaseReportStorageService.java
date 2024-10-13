package com.ukma.competition.platform.reports;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
// Реалізація функціоналу збереження звітів у базі даних, поки збереження відбувається лише для APPROVED
@Service
@ConditionalOnClass(name = "com.example.database.DatabaseService")
public class DatabaseReportStorageService implements ReportStorageService {

    private final ReportRepository reportRepository;

    // Конструктор для залежностей
    public DatabaseReportStorageService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public void storeReport(Report report) {
        if (report.getReportStatus() == ReportStatus.APPROVED) {
            reportRepository.save(report);
            System.out.println("Звіт збережено: " + report.getTopic());
        } else {
            // Логіка для випадку, якщо звіт не APPROVED
            System.out.println("Звіт не збережено, оскільки статус: " + report.getReportStatus());
        }
    }
}