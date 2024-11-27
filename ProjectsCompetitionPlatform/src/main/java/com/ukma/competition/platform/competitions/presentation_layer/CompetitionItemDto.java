package com.ukma.competition.platform.competitions.presentation_layer;

import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.projects.dto.ProjectListItemDto;
import com.ukma.competition.platform.shared.dto.BaseDto;
import com.ukma.competition.platform.users.dto.UserDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CompetitionItemDto extends BaseDto {

    String name;
    String description;
    Instant votingEndDate;
    ImageResponseDto logo;
    List<ProjectListItemDto> projects;
    boolean finished;
    UserDto organizer;
}
