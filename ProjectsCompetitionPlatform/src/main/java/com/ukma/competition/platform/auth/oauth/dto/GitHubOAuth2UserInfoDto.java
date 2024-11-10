package com.ukma.competition.platform.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ukma.competition.platform.auth.oauth.OAuth2UserInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GitHubOAuth2UserInfoDto implements OAuth2UserInfo {

    String login;
    String name;

    @JsonProperty("avatar_url")
    String avatar;

    String email;

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getUsername() {
        return StringUtils.isBlank(this.name) ? this.login : this.name;
    }

    @Override
    public String getAvatar() {
        return this.avatar;
    }
}
