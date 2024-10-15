package com.ukma.competition.platform.images;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukma.competition.platform.images.dto.ImageRequestDto;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.images.dto.ImageUpdateDto;
import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.shared.exception.FileEmptyException;
import com.ukma.competition.platform.shared.exception.ImageNotFoundException;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageServiceImpl extends GenericServiceImpl<Image, String, ImageRepository> implements ImageService {

    @Value("${spring.cloudinary.folder}")
    String CLOUDINARY_FOLDER;

    ObjectMapper objectMapper;

    CloudinaryService cloudinaryService;

    @Autowired
    public ImageServiceImpl(
        ImageRepository repository,
        ObjectMapper objectMapper,
        CloudinaryService cloudinaryService
    ) {
        super(repository);
        this.objectMapper = objectMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    @Transactional
    public ImageResponseDto uploadImage(ImageRequestDto imageRequestDto) throws IOException {
        MultipartFile imageFromRequest = imageRequestDto.getImage();
        if (imageFromRequest.isEmpty()) {
            throw new FileEmptyException(imageFromRequest.getOriginalFilename());
        } else {
            String url = cloudinaryService.upload(imageFromRequest, CLOUDINARY_FOLDER);
            Image newImage = Image.builder()
                .url(url)
                .build();
            super.save(newImage);

            return convertImageToDto(newImage);
        }
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
