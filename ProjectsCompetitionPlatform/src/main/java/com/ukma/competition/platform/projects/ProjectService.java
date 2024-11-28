package com.ukma.competition.platform.projects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ukma.competition.platform.comments.dto.CommentCreateDto;
import com.ukma.competition.platform.projects.dto.ProjectCreateUpdateDto;
import com.ukma.competition.platform.projects.dto.ProjectListDto;
import com.ukma.competition.platform.projects.dto.ProjectListItemDto;
import com.ukma.competition.platform.shared.GenericService;
import com.ukma.competition.platform.users.UserEntity;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProjectService extends GenericService<ProjectEntity, String> {

    void saveFromDto(ProjectCreateUpdateDto projectCreateDto, String userEmail) throws Exception;

    ProjectListDto findAllWithSearch(Pageable pageable, String search);

    ProjectListItemDto findOneAsDtoById(String id);

    ProjectListItemDto convertToDto(ProjectEntity project);

    ProjectCreateUpdateDto buildUpdateDto(String id) throws JsonProcessingException;

    void updateCallback(ProjectCreateUpdateDto projectCreateUpdateDto, String id) throws IOException;

    boolean addComment(CommentCreateDto commentCreateDto);

    List<ProjectListItemDto> findAllByCreator(UserEntity user);
}
