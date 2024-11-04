package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.JwtService;
import com.ukma.competition.platform.users.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;

public class DiscordOAuth2Service extends AbstractOAuth2Service {

    final String DISCORD_API_TOKEN_URL;
    final String DISCORD_API_USER_INFO_BASE_URL;
    final String DISCORD_AUTH_PAGE_URL;
    final String CLIENT_ID;
    final String CLIENT_SECRET;
    final String SCOPE;
    final String STATE;

    JwtService jwtService;
    UserService userService;

    public DiscordOAuth2Service(
        @Value("${oauth2.discord.url.apis.token}") String DISCORD_API_TOKEN_URL,
        @Value("${oauth2.discord.url.apis.user-info}") String DISCORD_API_USER_INFO_BASE_URL,
        @Value("${oauth2.discord.url.authPage}") String DISCORD_AUTH_PAGE_URL,
        @Value("${oauth2.discord.client.id}") String CLIENT_ID,
        @Value("${oauth2.discord.client.secret}") String CLIENT_SECRET,
        @Value("${oauth2.discord.scope}") String SCOPE,
        @Value("${oauth2.state}") String STATE,
        JwtService jwtService,
        UserService userService
    ) {
        this.DISCORD_API_TOKEN_URL = DISCORD_API_TOKEN_URL;
        this.DISCORD_API_USER_INFO_BASE_URL = DISCORD_API_USER_INFO_BASE_URL;
        this.DISCORD_AUTH_PAGE_URL = DISCORD_AUTH_PAGE_URL;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.SCOPE = SCOPE;
        this.STATE = STATE;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public String generateAuthenticationRedirectUrl() {
        return null;
    }

    @Override
    public Cookie authenticationCallback(String code) {
        return null;
    }

    @Override
    public AuthenticationProvider getOauthAuthenticationProvider() {
        return AuthenticationProvider.DISCORD;
    }
}
