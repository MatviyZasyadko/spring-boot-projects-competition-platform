package com.ukma.competition.platform.competitions.business_layer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CompetitionFilter {
    String name;
    String description;
}
