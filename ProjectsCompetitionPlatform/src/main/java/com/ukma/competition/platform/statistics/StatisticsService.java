package com.ukma.competition.platform.statistics;

import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.projects.ProjectService;
import com.ukma.competition.platform.votes.VoteService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StatisticsService {

    private final VoteService voteService;

    public StatisticsService(VoteService voteService) {
        this.voteService = voteService;
    }

    @Scheduled(cron = "0 0 8 * * MON")
    public void generateWeeklyStatistics() {
        List<Object[]> topProjects = voteService.findTop5ProjectsByVoteCount(5);
        System.out.println("Weekly statistics of most popular projects: ");
        for (Object[] topProject : topProjects) {
            ProjectEntity project = (ProjectEntity) topProject[0];
            int votesCount = (int) topProject[1];
            System.out.println("Project: " + project.getName() + ", votes: " + votesCount);
        }
    }
}
