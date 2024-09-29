package com.ukma.competition.platform.images;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukma.competition.platform.images.dto.ImageRequestDto;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.images.dto.ImageUpdateDto;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.shared.exception.FileEmptyException;
import com.ukma.competition.platform.shared.exception.ImageNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageServiceImpl extends GenericServiceImpl<Image, String, ImageRepository> implements ImageService {

    @Value("${cloudinary.url}")
    String CLOUDINARY_URL;

    ObjectMapper objectMapper;

    @Autowired
    public ImageServiceImpl(ImageRepository repository, ObjectMapper objectMapper) {
        super(repository);
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public ImageResponseDto uploadImage(ImageRequestDto imageRequestDto) throws IOException {
        MultipartFile imageFromRequest = imageRequestDto.getImage();
        if (imageFromRequest.isEmpty()) {
            throw new FileEmptyException(imageFromRequest.getOriginalFilename());
        } else {
            Image newImage = uploadImageApiRequest(imageFromRequest);
            super.save(newImage);

            return convertImageToDto(newImage);
        }
    }

    private Image uploadImageApiRequest(MultipartFile image) throws IOException {
        byte[] fileBytes = image.getBytes();
        byte[] fileBytesInBase64 = Base64.getEncoder().encode(fileBytes);
        String base64String = "data:" + image.getContentType() + ";base64," + new String(fileBytesInBase64);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("file", base64String);
        formData.add("upload_preset", "unsigned_preset");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(CLOUDINARY_URL, requestEntity, String.class);
        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        return Image.builder()
            .url(jsonNode.path("secure_url").asText())
            .publicId(jsonNode.path("public_id").asText())
            .build();
    }

    @Transactional(readOnly = true)
    public List<ImageResponseDto> findAllAsDto() {
        return super.findAll().stream()
            .map(this::convertImageToDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ImageResponseDto findByIdAsDto(String id) {
        return super.findById(id).map(this::convertImageToDto)
            .orElseThrow(() -> new ImageNotFoundException("Image is not found!", id));
    }

    @Override
    @Transactional
    public ImageResponseDto updateById(String id, ImageUpdateDto imageUpdateDto) {
        Image imageToUpdate = super.findById(id)
            .orElseThrow(() -> new ImageNotFoundException("Image is not found!", id));
        if (imageUpdateDto.getUrl() != null) {
            imageToUpdate.setUrl(imageUpdateDto.getUrl());
        }
        super.save(imageToUpdate);

        return convertImageToDto(imageToUpdate);
    }

    private ImageResponseDto convertImageToDto(Image image) {
        return ImageResponseDto
            .builder()
            .url(image.getUrl())
            .publicId(image.getPublicId())
            .id(image.getId())
            .createdAt(image.getCreatedAt())
            .updatedAt(image.getUpdatedAt())
            .build();
    }
}
