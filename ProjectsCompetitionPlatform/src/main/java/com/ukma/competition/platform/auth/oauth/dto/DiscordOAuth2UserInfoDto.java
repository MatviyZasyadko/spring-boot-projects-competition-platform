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
public class DiscordOAuth2UserInfoDto {

    String id;
    String username;
    String avatar;
    String email;
}
