package com.ukma.competition.platform.shared.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {

    public static String DISCORD_AVATAR_BASE_URL;

    public static String ACCESS_TOKEN_NAME;


    private AppConstants() {}


    @Value("${oauth2.discord.avatars.url}")
    public void setDiscordAvatarBaseUrl(String value) {
        DISCORD_AVATAR_BASE_URL = value;
    }

    @Value("${spring.security.access.token.name}")
    public void setAccessTokenName(String value) {
        ACCESS_TOKEN_NAME = value;
    }
}
