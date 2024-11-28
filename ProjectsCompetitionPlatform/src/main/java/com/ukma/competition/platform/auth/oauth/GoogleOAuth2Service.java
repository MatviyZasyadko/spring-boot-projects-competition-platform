package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.JwtService;
import com.ukma.competition.platform.auth.oauth.dto.GoogleOAuth2UserInfoDto;
import com.ukma.competition.platform.auth.oauth.dto.OAuth2TokensRequestDto;
import com.ukma.competition.platform.auth.oauth.dto.OAuth2TokensResponseDto;
import com.ukma.competition.platform.users.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Slf4j
public class GoogleOAuth2Service extends AbstractOAuth2Service {

    final String googleApiTokenUrl;
    final String googleApiUserInfoBaseUrl;

    public GoogleOAuth2Service(
        @Value("${oauth2.provider.google.url.apis.token}") String googleApiTokenBaseUrl,
        @Value("${oauth2.provider.google.url.apis.user-info}") String googleApiUserInfoBaseUrl,
        @Value("${oauth2.provider.google.url.authPage}") String googleAuthPage,
        @Value("${oauth2.provider.google.client.id}") String clientId,
        @Value("${oauth2.provider.google.client.secret}") String clientSecret,
        @Value("${oauth2.provider.google.scope}") String scope,
        @Value("${oauth2.state}") String state,
        JwtService jwtService,
        UserService userService
    ) {
        super(
            googleAuthPage,
            clientId,
            clientSecret,
            state,
            scope,
            userService,
            jwtService
        );
        this.googleApiTokenUrl = googleApiTokenBaseUrl;
        this.googleApiUserInfoBaseUrl = googleApiUserInfoBaseUrl;
    }

    @Override
    public OAuth2UserInfo getUserInfoFromResourceServer(String accessToken) {
        RestClient restClient = RestClient.create();

        return restClient.get()
            .uri(googleApiUserInfoBaseUrl + "/userinfo")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(GoogleOAuth2UserInfoDto.class);
    }

    @Override
    public OAuth2TokensResponseDto requestOAuth2Tokens(String code) {
        RestClient restClient = RestClient.create();
        OAuth2TokensRequestDto googleOAuth2RequestTokensDto = OAuth2TokensRequestDto.builder()
            .clientId(this.clientId)
            .clientSecret(this.clientSecret)
            .redirectUri(buildApplicationRedirectUrl())
            .grantType("authorization_code")
            .code(code)
            .build();

        return restClient.post()
            .uri(googleApiTokenUrl)
            .body(googleOAuth2RequestTokensDto)
            .retrieve()
            .body(OAuth2TokensResponseDto.class);
    }

    @Override
    public AuthenticationProvider getOAuth2AuthenticationProvider() {
        return AuthenticationProvider.GOOGLE;
    }
}
