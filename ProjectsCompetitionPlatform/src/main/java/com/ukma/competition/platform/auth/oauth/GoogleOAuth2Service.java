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

    final String GOOGLE_API_TOKEN_URL;
    final String GOOGLE_API_USER_INFO_BASE_URL;

    public GoogleOAuth2Service(
        @Value("${oauth2.provider.google.url.apis.token}") String GOOGLE_API_TOKEN_BASE_URL,
        @Value("${oauth2.provider.google.url.apis.user-info}") String GOOGLE_API_USER_INFO_BASE_URL,
        @Value("${oauth2.provider.google.url.authPage}") String GOOGLE_AUTH_PAGE,
        @Value("${oauth2.provider.google.client.id}") String CLIENT_ID,
        @Value("${oauth2.provider.google.client.secret}") String CLIENT_SECRET,
        @Value("${oauth2.provider.google.scope}") String SCOPE,
        @Value("${oauth2.state}") String STATE,
        JwtService jwtService,
        UserService userService
    ) {
        super(
            GOOGLE_AUTH_PAGE,
            CLIENT_ID,
            CLIENT_SECRET,
            STATE,
            SCOPE,
            userService,
            jwtService
        );
        this.GOOGLE_API_TOKEN_URL = GOOGLE_API_TOKEN_BASE_URL;
        this.GOOGLE_API_USER_INFO_BASE_URL = GOOGLE_API_USER_INFO_BASE_URL;
    }

    @Override
    public OAuth2UserInfo getUserInfoFromResourceServer(String accessToken) {
        RestClient restClient = RestClient.create();

        return restClient.get()
            .uri(GOOGLE_API_USER_INFO_BASE_URL + "/userinfo")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(GoogleOAuth2UserInfoDto.class);
    }

    @Override
    public OAuth2TokensResponseDto requestOAuth2Tokens(String code) {
        RestClient restClient = RestClient.create();
        OAuth2TokensRequestDto googleOAuth2RequestTokensDto = OAuth2TokensRequestDto.builder()
            .clientId(this.CLIENT_ID)
            .clientSecret(this.CLIENT_SECRET)
            .redirectUri(buildApplicationRedirectUrl())
            .grantType("authorization_code")
            .code(code)
            .build();

        return restClient.post()
            .uri(GOOGLE_API_TOKEN_URL)
            .body(googleOAuth2RequestTokensDto)
            .retrieve()
            .body(OAuth2TokensResponseDto.class);
    }

    @Override
    public AuthenticationProvider getOAuth2AuthenticationProvider() {
        return AuthenticationProvider.GOOGLE;
    }
}
