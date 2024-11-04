package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.projects.dto.ProjectCreateDto;
import com.ukma.competition.platform.shared.GenericService;

import java.io.IOException;

public interface ProjectService extends GenericService<ProjectEntity, String> {

    void saveFromDto(ProjectCreateDto projectCreateDto, String userEmail) throws IOException;
}
