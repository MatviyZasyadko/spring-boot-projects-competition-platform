package com.ukma.competition.platform.reports.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportCreateDto {

    @Length(min = 3, max = 30, message = "Topic value should contain from 3 to 30 characters!")
    String topic;

    @Length(min = 10, max = 250, message = "Report description value should contain from 10 to 250 characters!")
    String description;
}
