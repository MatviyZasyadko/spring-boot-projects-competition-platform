package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.projects.dto.ProjectCreateDto;
import com.ukma.competition.platform.projects.dto.ProjectListDto;
import com.ukma.competition.platform.projects.dto.ProjectListItemDto;
import com.ukma.competition.platform.shared.GenericService;
import org.springframework.data.domain.Pageable;

public interface ProjectService extends GenericService<ProjectEntity, String> {

    void saveFromDto(ProjectCreateDto projectCreateDto, String userEmail) throws Exception;
    ProjectListDto findAllWithSearch(Pageable pageable, String search);
    ProjectListItemDto findOneAsDtoById(String id);
}
