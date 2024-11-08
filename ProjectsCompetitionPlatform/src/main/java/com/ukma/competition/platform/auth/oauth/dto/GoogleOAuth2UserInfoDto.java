package com.ukma.competition.platform.auth.oauth.dto;

import com.ukma.competition.platform.auth.oauth.OAuth2UserInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GoogleOAuth2UserInfoDto implements OAuth2UserInfo {

    String email;
    String name;
    String picture;

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public String getAvatar() {
        return this.picture;
    }
}
