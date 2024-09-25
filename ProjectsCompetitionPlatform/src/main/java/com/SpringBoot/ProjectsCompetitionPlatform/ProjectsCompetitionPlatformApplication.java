package com.SpringBoot.ProjectsCompetitionPlatform;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import com.SpringBoot.ProjectsCompetitionPlatform.competitions.CompetitionService;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.ProjectService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProjectsCompetitionPlatformApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ProjectsCompetitionPlatformApplication.class, args);
		ProjectService projectService = applicationContext.getBean(ProjectService.class);
		CompetitionService competitionService = applicationContext.getBean(CompetitionService.class);
	}

}
