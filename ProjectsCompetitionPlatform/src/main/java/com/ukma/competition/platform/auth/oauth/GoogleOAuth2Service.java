package com.ukma.competition.platform.auth.oauth;

import com.ukma.competition.platform.auth.JwtService;
import com.ukma.competition.platform.auth.oauth.dto.GoogleOAuth2UserInfoDto;
import com.ukma.competition.platform.auth.oauth.dto.GoogleOAuth2TokensRequestDto;
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

    JwtService jwtService;
    UserService userService;

    public GoogleOAuth2Service(
        @Value("${oauth2.google.url.apis.token}") String GOOGLE_API_TOKEN_URL,
        @Value("${oauth2.google.url.apis.user-info}") String GOOGLE_API_USER_INFO_BASE_URL,
        @Value("${oauth2.google.url.authPage}") String GOOGLE_AUTH_PAGE,
        @Value("${oauth2.google.client.id}") String CLIENT_ID,
        @Value("${oauth2.google.client.secret}") String CLIENT_SECRET,
        @Value("${oauth2.google.scope}") String SCOPE,
        @Value("${oauth2.state}") String STATE,
        JwtService jwtService,
        UserService userService
    ) {
        super(
            GOOGLE_AUTH_PAGE,
            CLIENT_ID,
            CLIENT_SECRET,
            STATE,
            SCOPE
        );
        this.jwtService = jwtService;
        this.userService = userService;
        this.GOOGLE_API_TOKEN_URL = GOOGLE_API_TOKEN_URL;
        this.GOOGLE_API_USER_INFO_BASE_URL = GOOGLE_API_USER_INFO_BASE_URL;
    }

    @Override
    public Cookie authenticationCallback(String code) {
        try {
            log.info("Starting the processing of OAuth2 callback for provider {}", this.getOAuth2AuthenticationProvider());
            OAuth2TokensResponseDto tokensResponseDto = requestGoogleTokens(code);
            GoogleOAuth2UserInfoDto userInfoFromResourceServer = getUserInfoFromResourceServer(tokensResponseDto.getAccessToken());

            validateUserInfo(userInfoFromResourceServer);

            UserEntity userCheck = userService.findByEmail(userInfoFromResourceServer.getEmail()).orElse(null);

            if (userCheck == null) {
                userCheck = UserEntity.builder()
                    .email(userInfoFromResourceServer.getEmail())
                    .fullName(userInfoFromResourceServer.getName())
                    .authenticationProvider(this.getOAuth2AuthenticationProvider())
                    .build();
                if (userInfoFromResourceServer.getPicture() != null) {
                    ImageEntity image = ImageEntity.builder()
                        .url(userInfoFromResourceServer.getPicture())
                        .build();
                    userCheck.addImage(image);
                }
                userService.save(userCheck);
            }

            return this.jwtService.generateTokenWithCookie(userCheck);
        } catch (Exception exception) {
            log.error("Failed an attempt to authorize user through OAuth2 provider {}", this.getOAuth2AuthenticationProvider());
            throw new AuthenticationException("Some error occurred during Google authentication", exception);
        }
    }

    private OAuth2TokensResponseDto requestGoogleTokens(String code) {
        RestClient restClient = RestClient.create();
        GoogleOAuth2TokensRequestDto googleOAuth2RequestTokensDto = GoogleOAuth2TokensRequestDto.builder()
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

    private GoogleOAuth2UserInfoDto getUserInfoFromResourceServer(String accessToken) {
        RestClient restClient = RestClient.create();

        return restClient.get()
            .uri(GOOGLE_API_USER_INFO_BASE_URL + "/userinfo")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(GoogleOAuth2UserInfoDto.class);
    }

    private void validateUserInfo(GoogleOAuth2UserInfoDto googleUserInfoDto) {
        if (StringUtils.isBlank(googleUserInfoDto.getEmail()) || StringUtils.isBlank(googleUserInfoDto.getName())) {
            throw new AuthenticationException("OAuth2 error: user info does not contain");
        }
    }

    @Override
    public AuthenticationProvider getOAuth2AuthenticationProvider() {
        return AuthenticationProvider.GOOGLE;
    }
}
