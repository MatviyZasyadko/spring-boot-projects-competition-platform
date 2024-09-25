package com.SpringBoot.ProjectsCompetitionPlatform.projects;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;

    @Override
    public void save(Project project) {
        projectRepository.save(project);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project findOneById(String id) {
        return projectRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(String id) {
        projectRepository.deleteById(id);
    }
}
