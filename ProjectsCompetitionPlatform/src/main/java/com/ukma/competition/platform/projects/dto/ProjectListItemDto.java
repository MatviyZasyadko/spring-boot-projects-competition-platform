package com.ukma.competition.platform.projects.dto;

import com.ukma.competition.platform.comments.dto.CommentDto;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.users.dto.UserDto;
import lombok.*;
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
    List<CommentDto> comments;
    Integer votesAmount = 0;
}
