package com.ukma.competition.platform.shared.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {

    public static String discordAvatarBaseUrl = null;

    public static String accessTokenName;

    public static String cloudinaryFolder;

    private AppConstants() {}

    @Value("${oauth2.provider.discord.url.avatars}")
    public void setDiscordAvatarBaseUrl(String value) {
        discordAvatarBaseUrl = value;
    }

    @Value("${spring.security.access.token.name}")
    public void setAccessTokenName(String value) {
        accessTokenName = value;
    }

    @Value("${spring.cloudinary.folder}")
    public void setCloudinaryFolder(String value) {
        cloudinaryFolder = value;
    }
}
