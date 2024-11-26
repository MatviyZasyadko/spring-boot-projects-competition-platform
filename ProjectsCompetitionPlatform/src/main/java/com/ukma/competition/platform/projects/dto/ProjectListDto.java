package com.ukma.competition.platform.projects.dto;

import com.ukma.competition.platform.shared.dto.PaginationDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectListDto {

    List<ProjectListItemDto> content;
    List<PaginationDto> paginationDtoList;
    Integer totalPages;
}
