package com.ukma.competition.platform.reports;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl extends GenericServiceImpl <Report, String, ReportRepository> implements ReportService {

    @Autowired
    public ReportServiceImpl(ReportRepository repository) {
        super(repository);
    }
}
