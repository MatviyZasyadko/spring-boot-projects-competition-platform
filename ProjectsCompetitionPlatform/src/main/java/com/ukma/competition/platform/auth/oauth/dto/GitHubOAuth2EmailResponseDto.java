package com.ukma.competition.platform.auth.oauth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GitHubOAuth2EmailResponseDto {

    String email;
    Boolean primary;
    Boolean verified;
}
