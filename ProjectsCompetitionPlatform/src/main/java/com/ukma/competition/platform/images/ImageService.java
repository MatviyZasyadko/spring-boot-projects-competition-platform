package com.ukma.competition.platform.images;

import com.ukma.competition.platform.images.dto.ImageRequestDto;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.images.dto.ImageUpdateDto;
import com.ukma.competition.platform.shared.GenericService;

import java.io.IOException;
import java.util.List;

public interface ImageService extends GenericService<ImageEntity, String> {

    ImageResponseDto uploadImage(ImageRequestDto imageRequestDto) throws IOException;
    List<ImageResponseDto> findAllAsDto();
    ImageResponseDto findByIdAsDto(String id);
    ImageResponseDto updateById(String id, ImageUpdateDto imageUpdateDto);
}
