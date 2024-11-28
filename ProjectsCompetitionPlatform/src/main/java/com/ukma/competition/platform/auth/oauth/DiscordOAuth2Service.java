package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.JwtService;
import com.ukma.competition.platform.auth.oauth.dto.DiscordOAuth2UserInfoDto;
import com.ukma.competition.platform.auth.oauth.dto.OAuth2TokensResponseDto;
import com.ukma.competition.platform.users.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    String discordApiTokenUrl;
    String discordApiUserInfo;

    public DiscordOAuth2Service(
        @Value("${oauth2.provider.discord.url.apis.token}") String discordApiTokenUrl,
        @Value("${oauth2.provider.discord.url.apis.user-info}") String discordApiUserInfo,
        @Value("${oauth2.provider.discord.url.authPage}") String discordAuthPageUrl,
        @Value("${oauth2.provider.discord.client.id}") String clientId,
        @Value("${oauth2.provider.discord.client.secret}") String clientSecret,
        @Value("${oauth2.provider.discord.scope}") String scope,
        @Value("${oauth2.state}") String state,
        JwtService jwtService,
        UserService userService
    ) {
        super(
            discordAuthPageUrl,
            clientId,
            clientSecret,
            state,
            scope,
            userService,
            jwtService
        );
        this.discordApiTokenUrl = discordApiTokenUrl;
        this.discordApiUserInfo = discordApiUserInfo;
    }

    @Override
    public AuthenticationProvider getOAuth2AuthenticationProvider() {
        return AuthenticationProvider.DISCORD;
    }

    @Override
    public OAuth2TokensResponseDto requestOAuth2Tokens(String code) {
        RestClient restClient = RestClient.create();

        return restClient.post()
            .uri(discordApiTokenUrl, generateBodyForTokenRequest(code))
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
            .uri(discordApiUserInfo)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(DiscordOAuth2UserInfoDto.class);
    }

    private MultiValueMap<String, String> generateBodyForTokenRequest(String code) {
        LinkedMultiValueMap<String, String> tokenRequestMap = new LinkedMultiValueMap<>();

        tokenRequestMap.add("client_id", clientId);
        tokenRequestMap.add("client_secret", clientSecret);
        tokenRequestMap.add("grant_type", "authorization_code");
        tokenRequestMap.add("code", code);
        tokenRequestMap.add("redirect_uri", buildApplicationRedirectUrl());

        return tokenRequestMap;
    }
}
