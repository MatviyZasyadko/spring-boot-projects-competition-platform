package com.ukma.competition.platform.comments.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentCreateDto {
    @Length(min = 1, max = 250, message = "Comment text value should contain from 1 to 250 characters!")
    String text;

    String projectId;
}
