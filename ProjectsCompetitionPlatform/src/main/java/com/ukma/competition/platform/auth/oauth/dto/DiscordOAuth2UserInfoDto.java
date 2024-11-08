package com.ukma.competition.platform.auth.oauth.dto;


import com.ukma.competition.platform.auth.oauth.OAuth2UserInfo;
import com.ukma.competition.platform.shared.constants.AppConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DiscordOAuth2UserInfoDto implements OAuth2UserInfo {

    String id;
    String username;
    String avatar;
    String email;

    @Override
    public String getAvatar() {
        return this.avatar == null
            ? null
            : AppConstants.DISCORD_AVATAR_BASE_URL + this.id + "/" + this.avatar;
    }
}
