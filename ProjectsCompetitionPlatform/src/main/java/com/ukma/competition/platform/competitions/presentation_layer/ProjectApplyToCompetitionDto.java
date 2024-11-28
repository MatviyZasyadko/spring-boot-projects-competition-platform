package com.ukma.competition.platform.competitions.presentation_layer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProjectApplyToCompetitionDto {

    String projectId;
}
