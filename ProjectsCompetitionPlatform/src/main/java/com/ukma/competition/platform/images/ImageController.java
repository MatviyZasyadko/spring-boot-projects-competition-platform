package com.ukma.competition.platform.images;

import com.ukma.competition.platform.images.dto.ImageRequestDto;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.images.dto.ImageUpdateDto;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ImageController {

    ImageService imageService;

    @PostMapping("/_upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ImageResponseDto save(@ModelAttribute @Valid ImageRequestDto imageRequestDto) throws IOException {
        return imageService.uploadImage(imageRequestDto);
    }

    @GetMapping
    public List<ImageResponseDto> findAll() {
        return imageService.findAllAsDto();
    }

    @GetMapping("/{id}")
    public ImageResponseDto findById(@PathVariable("id") String id) {
        return imageService.findByIdAsDto(id);
    }

    @PutMapping("/{id}")
    public ImageResponseDto updateById(
        @PathVariable("id") String id,
        @RequestBody @Valid ImageUpdateDto imageUpdateDto
    ) {
        return imageService.updateById(id, imageUpdateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") String id) {
        imageService.deleteById(id);
    }
}
