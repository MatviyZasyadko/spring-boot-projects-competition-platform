package com.SpringBoot.ProjectsCompetitionPlatform;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import com.SpringBoot.ProjectsCompetitionPlatform.competitions.CompetitionService;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import com.SpringBoot.ProjectsCompetitionPlatform.projects.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
public class ProjectsCompetitionPlatformApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ProjectsCompetitionPlatformApplication.class, args);
		ProjectService projectService = applicationContext.getBean(ProjectService.class);
		CompetitionService competitionService = applicationContext.getBean(CompetitionService.class);

		String newCompetitionName = "new competition";
		Competition newCompetition = new Competition(
			newCompetitionName,
			"description",
			new Date(),
			3000
		);
		if (competitionService.create(newCompetition)) {
			Competition competition = competitionService.getOneByName(newCompetitionName);
			System.out.println("Created new competition");
			System.out.println(competition);
			competition.setDescription("new description");
			competitionService.updateByName(competition.getName(), competition);
			Competition updatedCompetition = competitionService.getOneByName(newCompetitionName);
			System.out.println("Updated competition");
			System.out.println(updatedCompetition);
			Project newProject = new Project(
				"some new project",
				new ArrayList<>(),
				newCompetition
			);
			if (projectService.create(newProject)) {
				System.out.println("created new project");
				System.out.println(projectService.getByName(newProject.getName()));
				System.out.println("Get all projects by competition name");
				System.out.println(projectService.getAllByCompetitionName(competition.getName()));
				System.out.println("Competition after creation of project");
				System.out.println(updatedCompetition);
			}
		}
	}

}
