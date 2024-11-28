package com.ukma.competition.platform.comments;

import com.ukma.competition.platform.comments.dto.CommentDto;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.projects.dto.ProjectListItemDto;
import com.ukma.competition.platform.shared.GenericService;

public interface CommentService extends GenericService<CommentEntity, String> {
    CommentDto convertToDto(CommentEntity comment);
}
