package com.ukma.competition.platform.statistics;

import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.projects.ProjectService;
import com.ukma.competition.platform.votes.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class StatisticsService {

    private final VoteService voteService;

    public StatisticsService(VoteService voteService) {
        this.voteService = voteService;
    }

    @Scheduled(cron = "0 0 8 * * MON")
    public void generateWeeklyStatistics() {
        List<Object[]> topProjects = voteService.findTop5ProjectsByVoteCount(5);
        log.info("Weekly statistics of most popular projects: ");
        for (Object[] topProject : topProjects) {
            ProjectEntity project = (ProjectEntity) topProject[0];
            int votesCount = (int) topProject[1];
            log.info("Project: " + project.getName() + ", votes: " + votesCount);
        }
    }
}
