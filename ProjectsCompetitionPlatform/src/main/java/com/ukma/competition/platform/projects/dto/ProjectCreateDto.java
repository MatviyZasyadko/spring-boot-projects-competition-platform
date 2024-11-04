package com.ukma.competition.platform.projects.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectCreateDto {

    @Length(min = 3, max = 30, message = "Name value should contain from 3 to 30 characters!")
    String name;

    @Length(min = 10, max = 250, message = "Project short description value should contain from 10 to 250 characters!")
    String shortDescription;

    @Length(min = 20, max = 500, message = "Project full description value should contain from 20 to 500 characters!")
    String fullDescription;

    MultipartFile logo;
}
