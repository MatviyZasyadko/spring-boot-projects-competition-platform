package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.JwtService;
import com.ukma.competition.platform.auth.oauth.dto.DiscordOAuth2UserInfoDto;
import com.ukma.competition.platform.auth.oauth.dto.OAuth2TokensResponseDto;
import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.shared.exception.AuthenticationException;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DiscordOAuth2Service extends AbstractOAuth2Service {

    String DISCORD_API_TOKEN_URL;
    String DISCORD_API_USER_INFO;

    public DiscordOAuth2Service(
        @Value("${oauth2.provider.discord.url.apis.token}") String DISCORD_API_TOKEN_URL,
        @Value("${oauth2.provider.discord.url.apis.user-info}") String DISCORD_API_USER_INFO,
        @Value("${oauth2.provider.discord.url.authPage}") String DISCORD_AUTH_PAGE_URL,
        @Value("${oauth2.provider.discord.client.id}") String CLIENT_ID,
        @Value("${oauth2.provider.discord.client.secret}") String CLIENT_SECRET,
        @Value("${oauth2.provider.discord.scope}") String SCOPE,
        @Value("${oauth2.state}") String STATE,
        @Value("oauth2.provider.discord.url.avatars") String AVATARS_URL,
        JwtService jwtService,
        UserService userService
    ) {
        super(
            DISCORD_AUTH_PAGE_URL,
            CLIENT_ID,
            CLIENT_SECRET,
            STATE,
            SCOPE,
            userService,
            jwtService
        );
        this.DISCORD_API_TOKEN_URL = DISCORD_API_TOKEN_URL;
        this.DISCORD_API_USER_INFO = DISCORD_API_USER_INFO;
    }

    @Override
    public AuthenticationProvider getOAuth2AuthenticationProvider() {
        return AuthenticationProvider.DISCORD;
    }

    @Override
    public OAuth2TokensResponseDto requestOAuth2Tokens(String code) {
        RestClient restClient = RestClient.create();

        return restClient.post()
            .uri(DISCORD_API_TOKEN_URL, generateBodyForTokenRequest(code))
            .body(generateBodyForTokenRequest(code))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .header(HttpHeaders.ACCEPT_ENCODING, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .retrieve()
            .body(OAuth2TokensResponseDto.class);
    }

    @Override
    public OAuth2UserInfo getUserInfoFromResourceServer(String accessToken) {
        RestClient restClient = RestClient.create();

        return restClient.get()
            .uri(DISCORD_API_USER_INFO)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(DiscordOAuth2UserInfoDto.class);
    }

    private MultiValueMap<String, String> generateBodyForTokenRequest(String code) {
        return new LinkedMultiValueMap<>() {{
            add("client_id", CLIENT_ID);
            add("client_secret", CLIENT_SECRET);
            add("grant_type", "authorization_code");
            add("code", code);
            add("redirect_uri", buildApplicationRedirectUrl());
        }};
    }
}
