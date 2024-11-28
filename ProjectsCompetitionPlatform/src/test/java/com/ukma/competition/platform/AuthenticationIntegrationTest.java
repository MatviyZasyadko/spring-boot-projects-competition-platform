package com.ukma.competition.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukma.competition.platform.auth.EndpointConstants;
import com.ukma.competition.platform.auth.dto.LoginRequestDto;
import com.ukma.competition.platform.auth.dto.RegistrationRequestDto;
import com.ukma.competition.platform.config.AuthenticationTestConfig;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserRepository;
import com.ukma.competition.platform.users.UserResponseValidator;
import com.ukma.competition.platform.users.UserRole;
import com.ukma.competition.platform.users.dto.UserResponseDto;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(AuthenticationTestConfig.class)
class AuthenticationIntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    UserResponseValidator userResponseValidator;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    MockMvc mockMvc;

    static final String ACCESS_TOKEN_COOKIE_NAME = "COMPETITORS_ACCESS_TOKEN";

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
    }

    @Test
    void registrationWithCorrectDataIsSuccessful() throws Exception {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
            "Vova Havryliuk",
            "Vova@mail.com",
            "vova123"
        );

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(
            UserEntity.builder()
                .email(registrationRequestDto.getEmail())
                .fullName("registrationRequestDto.getFullName()")
                .userRole(UserRole.USER)
                .build()
        );

        mockMvc.perform(
                post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationRequestDto))
            )
            .andExpect(status().isOk())
            .andExpect(cookie().exists(ACCESS_TOKEN_COOKIE_NAME));

        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void registrationIsFailedIfUserWithSuchUsernameAlreadyExists() throws Exception {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
            "Vova Havryliuk",
            "Vova@mail.com",
            "vova123"
        );

        when(userRepository.findByEmail(any(String.class))).thenReturn(
            Optional.of(UserEntity.builder()
                .email(registrationRequestDto.getEmail())
                .password(registrationRequestDto.getPassword())
                .userRole(UserRole.USER)
                .build()
            )
        );

        mockMvc.perform(
                post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationRequestDto))
            )
            .andExpect(status().isBadRequest());

        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void loginIsSuccessfulIfUserRegistered() throws Exception {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
            "Vova Havryliuk",
            "Vova@mail.com",
            "vova123"
        );

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(
            UserEntity.builder()
                .email(registrationRequestDto.getEmail())
                .fullName("registrationRequestDto.getFullName()")
                .password(registrationRequestDto.getPassword())
                .userRole(UserRole.USER)
                .build()
        );

        mockMvc.perform(
                post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationRequestDto))
            )
            .andExpect(status().isOk())
            .andExpect(cookie().exists(ACCESS_TOKEN_COOKIE_NAME));

        when(userRepository.findByEmail(any(String.class))).thenReturn(
            Optional.of(UserEntity.builder()
                .email(registrationRequestDto.getEmail())
                .password(registrationRequestDto.getPassword())
                .userRole(UserRole.USER)
                .build()
            )
        );
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        LoginRequestDto loginRequestDto = new LoginRequestDto(registrationRequestDto.getEmail(), registrationRequestDto.getPassword());
        mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequestDto))
            )
            .andExpect(status().isOk())
            .andExpect(cookie().exists(ACCESS_TOKEN_COOKIE_NAME))
            .andReturn();

        verify(userRepository, times(2)).findByEmail(any(String.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
    }

    @Test
    void loginIsFailedIfUserIsNotRegistered() throws Exception {
        LoginRequestDto registrationRequestDto = new LoginRequestDto(
            "Vova@mail.com",
            "vova123"
        );

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequestDto))
        ).andExpect(status().isBadRequest());

        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void simpleUserCanAccessAuthenticatedResources() throws Exception {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
            "Vova Havryliuk",
            "Vova@mail.com",
            "vova123"
        );

        when(userRepository.findByEmail(any(String.class))).thenReturn(
            Optional.of(UserEntity.builder()
                .email(registrationRequestDto.getEmail())
                .password(registrationRequestDto.getPassword())
                .userRole(UserRole.USER)
                .build()
            )
        );
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        MvcResult loginRequestResult =  mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationRequestDto))
            )
            .andExpect(status().isOk())
            .andExpect(cookie().exists(ACCESS_TOKEN_COOKIE_NAME))
            .andReturn();

        String jwt = loginRequestResult.getResponse().getCookie(ACCESS_TOKEN_COOKIE_NAME).getValue();

        //accessing authenticated resource
        MvcResult profileRequestResult = mockMvc.perform(
                get("/api/auth/profile")
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, jwt))
            )
            .andExpect(status().isOk())
            .andReturn();

        UserResponseDto userResponseDto = objectMapper.readValue(profileRequestResult.getResponse().getContentAsString(), UserResponseDto.class);

        assertThat(userResponseDto).isNotNull();
        assertThat(userResponseValidator.validate(userResponseDto)).isTrue();

        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void adminCanAccessAuthenticatedResources() throws Exception {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
            "Vova Havryliuk",
            "Vova@mail.com",
            "vova123"
        );

        when(userRepository.findByEmail(any(String.class))).thenReturn(
            Optional.of(UserEntity.builder()
                .email(registrationRequestDto.getEmail())
                .password(registrationRequestDto.getPassword())
                .userRole(UserRole.ADMIN)
                .build()
            )
        );
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        MvcResult loginRequestResult =  mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationRequestDto))
            )
            .andExpect(status().isOk())
            .andExpect(cookie().exists(ACCESS_TOKEN_COOKIE_NAME))
            .andReturn();

        String jwt = loginRequestResult.getResponse().getCookie(ACCESS_TOKEN_COOKIE_NAME).getValue();

        //accessing authenticated resource
        MvcResult profileRequestResult = mockMvc.perform(
                get("/api/auth/profile")
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, jwt))
            )
            .andExpect(status().isOk())
            .andReturn();

        UserResponseDto userResponseDto = objectMapper.readValue(profileRequestResult.getResponse().getContentAsString(), UserResponseDto.class);

        assertThat(userResponseDto).isNotNull();
        assertThat(userResponseValidator.validate(userResponseDto)).isTrue();

        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void adminCanAccessAuthenticatedResourcesOnlyForAdmins() throws Exception {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
            "Vova Havryliuk",
            "Vova@mail.com",
            "vova123"
        );

        when(userRepository.findByEmail(any(String.class))).thenReturn(
            Optional.of(UserEntity.builder()
                .email(registrationRequestDto.getEmail())
                .password(registrationRequestDto.getPassword())
                .userRole(UserRole.ADMIN)
                .build()
            )
        );
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        MvcResult loginRequestResult =  mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationRequestDto))
            )
            .andExpect(status().isOk())
            .andExpect(cookie().exists(ACCESS_TOKEN_COOKIE_NAME))
            .andReturn();

        String jwt = loginRequestResult.getResponse().getCookie(ACCESS_TOKEN_COOKIE_NAME).getValue();

        //accessing authenticated resource
        MvcResult profileRequestResult = mockMvc.perform(
                get("/api/auth/profile/admin")
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, jwt))
            )
            .andExpect(status().isOk())
            .andReturn();

        UserResponseDto userResponseDto = objectMapper.readValue(profileRequestResult.getResponse().getContentAsString(), UserResponseDto.class);

        assertThat(userResponseDto).isNotNull();
        assertThat(userResponseValidator.validate(userResponseDto)).isTrue();

        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void simpleUserCanNotAccessAuthenticatedResourcesOnlyForAdmins() throws Exception {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
            "Vova Havryliuk",
            "Vova@mail.com",
            "vova123"
        );

        when(userRepository.findByEmail(any(String.class))).thenReturn(
            Optional.of(UserEntity.builder()
                .email(registrationRequestDto.getEmail())
                .password(registrationRequestDto.getPassword())
                .userRole(UserRole.USER)
                .build()
            )
        );
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        MvcResult loginRequestResult =  mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationRequestDto))
            )
            .andExpect(status().isOk())
            .andExpect(cookie().exists(ACCESS_TOKEN_COOKIE_NAME))
            .andReturn();

        String jwt = loginRequestResult.getResponse().getCookie(ACCESS_TOKEN_COOKIE_NAME).getValue();

        //accessing authenticated resource
        mockMvc.perform(
                get("/api/auth/profile/admin").cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, jwt))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticatedUserCanNotAccessAuthenticatedResources() throws Exception {
        //accessing authenticated resource without jwt in cookies
        mockMvc.perform(
                get("/api/auth/profile")
            )
            .andExpect(status().isFound()) // redirecting user on login page, because he is not authenticated
            .andExpect(header().stringValues(HttpHeaders.LOCATION, EndpointConstants.getContextPath() + EndpointConstants.LOGIN_PAGE_ENDPOINT));
    }
}
