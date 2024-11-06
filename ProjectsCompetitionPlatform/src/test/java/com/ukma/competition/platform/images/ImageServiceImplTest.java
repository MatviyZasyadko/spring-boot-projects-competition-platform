package com.ukma.competition.platform.images;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukma.competition.platform.images.dto.ImageRequestDto;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.images.dto.ImageUpdateDto;
import com.ukma.competition.platform.shared.exception.FileEmptyException;
import com.ukma.competition.platform.shared.exception.ImageNotFoundException;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Value("${spring.cloudinary.folder}")
    private String CLOUDINARY_FOLDER;

    private final static String IMAGE_ID = "imageId123";
    private final static String IMAGE_URL = "http://example.com/image.jpg";

    @BeforeEach
    void setUp() {
        imageService = new ImageServiceImpl(imageRepository, new ObjectMapper(), cloudinaryService);
    }

    @Test
    void uploadImageSuccess() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        ImageRequestDto imageRequestDto = new ImageRequestDto(mockFile);

        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(cloudinaryService.upload(any(), any())).thenReturn(IMAGE_URL);

        ImageResponseDto result = imageService.uploadImage(imageRequestDto);

        assertThat(result.getUrl()).isEqualTo(IMAGE_URL);
    }

    @Test
    void uploadImageFileEmptyException() {
        MultipartFile mockFile = mock(MultipartFile.class);
        ImageRequestDto imageRequestDto = new ImageRequestDto(mockFile);

        when(mockFile.isEmpty()).thenReturn(true);

        assertThatThrownBy(() -> imageService.uploadImage(imageRequestDto))
            .isInstanceOf(FileEmptyException.class);
        verify(imageRepository, never()).save(any(ImageEntity.class));
    }

    @Test
    void findAllAsDtoReturnsAllImages() {
        ImageEntity image = ImageEntity.builder().url(IMAGE_URL).build();
        when(imageRepository.findAll()).thenReturn(List.of(image));

        List<ImageResponseDto> result = imageService.findAllAsDto();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUrl()).isEqualTo(IMAGE_URL);
    }

    @Test
    void findByIdAsDtoSuccess() {
        ImageEntity image = ImageEntity.builder().url(IMAGE_URL).build();
        image.setId(IMAGE_ID);
        when(imageRepository.findById(IMAGE_ID)).thenReturn(Optional.of(image));

        ImageResponseDto result = imageService.findByIdAsDto(IMAGE_ID);

        assertThat(result.getId()).isEqualTo(IMAGE_ID);
        assertThat(result.getUrl()).isEqualTo(IMAGE_URL);
    }

    @Test
    void findByIdAsDtoImageNotFoundException() {
        when(imageRepository.findById(IMAGE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.findByIdAsDto(IMAGE_ID))
            .isInstanceOf(ImageNotFoundException.class)
            .hasMessageContaining("Image is not found!");
    }

    @Test
    void updateByIdSuccess() {
        ImageEntity image = ImageEntity.builder().url(IMAGE_URL).build();
        image.setId(IMAGE_ID);
        ImageUpdateDto imageUpdateDto = new ImageUpdateDto("http://example.com/updated.jpg");

        when(imageRepository.findById(IMAGE_ID)).thenReturn(Optional.of(image));

        ImageResponseDto result = imageService.updateById(IMAGE_ID, imageUpdateDto);

        assertThat(result.getUrl()).isEqualTo(imageUpdateDto.getUrl());
    }

    @Test
    void updateByIdImageNotFoundException() {
        ImageUpdateDto imageUpdateDto = new ImageUpdateDto("http://example.com/updated.jpg");
        when(imageRepository.findById(IMAGE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.updateById(IMAGE_ID, imageUpdateDto))
            .isInstanceOf(ImageNotFoundException.class)
            .hasMessageContaining("Image is not found!");
    }
}