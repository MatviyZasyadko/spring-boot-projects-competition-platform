package com.ukma.competition.platform.comments.dto;

import com.ukma.competition.platform.users.UserEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    UserEntity author;
    String text;
    String createdAt;
}
