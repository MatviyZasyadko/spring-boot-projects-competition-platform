package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectServiceImpl extends GenericServiceImpl<Project, String, ProjectRepository> implements ProjectService {

    @Autowired
    public ProjectServiceImpl(ProjectRepository repository) {
        super(repository);
    }
}
