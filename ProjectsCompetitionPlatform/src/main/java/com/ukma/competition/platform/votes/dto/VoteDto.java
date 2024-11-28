package com.ukma.competition.platform.votes.dto;

import com.ukma.competition.platform.projects.dto.ProjectListItemDto;
import com.ukma.competition.platform.users.dto.UserDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

    UserDto user;
    String projectId;
}
