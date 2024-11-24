package com.ukma.competition.platform.projects.dto;

import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.users.dto.UserDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectRecordDto {

    String id;
    String name;
    String shortDescription;
    String fullDescription;
    ImageResponseDto logo;
    UserDto creator;
}
