package com.ukma.competition.platform;

import com.ukma.competition.platform.auth.JwtService;
import com.ukma.competition.platform.images.ImageController;
import com.ukma.competition.platform.images.ImageService;
import com.ukma.competition.platform.images.dto.ImageRequestDto;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.shared.exception.ImageNotFoundException;
import com.ukma.competition.platform.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
@AutoConfigureMockMvc(addFilters = false)
class ImageControllerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImageService imageService;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;


    @Test
    @WithMockUser(roles = "ADMIN")
    void saveImageSuccessfully() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", new byte[]{});
        ImageResponseDto responseDto = ImageResponseDto.builder()
            .id("image-id")
            .url("http://image.url")
            .build();
        Mockito.when(imageService.uploadImage(any(ImageRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(multipart("/api/images/_upload")
                .file(mockMultipartFile))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("image-id"))
            .andExpect(jsonPath("$.url").value("http://image.url"));
    }

    @Test
    void findAllImagesSuccessfully() throws Exception {
        List<ImageResponseDto> responseDtos = List.of(ImageResponseDto.builder()
                .id("image-id-1")
                .url("http://image.url")
                .build(),
            ImageResponseDto.builder()
                .id("image-id-2")
                .url("http://image.url-2")
                .build());
        Mockito.when(imageService.findAllAsDto()).thenReturn(responseDtos);

        mockMvc.perform(get("/api/images"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].id").value("image-id-1"))
            .andExpect(jsonPath("$[1].id").value("image-id-2"));
    }

    @Test
    void findImageByIdSuccessfully() throws Exception {
        ImageResponseDto responseDto = ImageResponseDto.builder()
            .id("image-id")
            .url("http://image.url")
            .build();
        Mockito.when(imageService.findByIdAsDto(anyString())).thenReturn(responseDto);

        mockMvc.perform(get("/api/images/image-id"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("image-id"))
            .andExpect(jsonPath("$.url").value("http://image.url"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteImageByIdSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/images/image-id"))
            .andExpect(status().isNoContent());
        Mockito.verify(imageService, Mockito.times(1)).deleteById("image-id");
    }

    @Test
    void findImageByIdThrowsNotFoundError() throws Exception {
        Mockito.when(imageService.findByIdAsDto(anyString())).thenThrow(new ImageNotFoundException("Image not found", "id"));

        mockMvc.perform(get("/api/images/invalid-id"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").exists());
    }
}
