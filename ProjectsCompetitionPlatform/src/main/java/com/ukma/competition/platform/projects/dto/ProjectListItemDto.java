package com.ukma.competition.platform.projects.dto;

import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.users.dto.UserDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectListItemDto {

    String id;
    String name;
    String shortDescription;
    String fullDescription;
    ImageResponseDto logo;
    List<ImageResponseDto> images;
    UserDto creator;
    Instant createdAt;
    Integer votesAmount = 0;
}
