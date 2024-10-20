package com.ukma.competition.platform.images;

import com.ukma.competition.platform.images.dto.ImageRequestDto;
import com.ukma.competition.platform.images.dto.ImageResponseDto;
import com.ukma.competition.platform.images.dto.ImageUpdateDto;
import com.ukma.competition.platform.shared.dto.exception.FileEmptyExceptionDto;
import com.ukma.competition.platform.shared.dto.exception.ValidationFailDto;
import com.ukma.competition.platform.shared.exception.ImageNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Images")
@ConditionalOnExpression("#{${image.rest.controller.enabled}==true}")
public class ImageController {

    ImageService imageService;
    private static final Logger logger = LogManager.getLogger(ImageController.class);

    @Operation(
        description = "Uploads image from form-data as a request body to the cloud storage \"Cloudinary\"",
        responses = {
            @ApiResponse(
                description = "Successfully uploaded",
                responseCode = "201",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ImageResponseDto.class)
                )
            ),
            @ApiResponse(
                description = "Bad request, when a provided file is empty",
                responseCode = "400",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FileEmptyExceptionDto.class)
                )
            ),
            @ApiResponse(
                description = "Bad request, when a provided file is null",
                responseCode = "400",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ValidationFailDto.class)
                )
            )
        }
    )
    @PostMapping("/_upload")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ImageResponseDto save(@ModelAttribute @Valid ImageRequestDto imageRequestDto) throws IOException {
        return imageService.uploadImage(imageRequestDto);
    }

    @Operation(
        description = "Returns a list of images records from db, including external URL from \"Cloudinary\" and file id.",
        responses = {
            @ApiResponse(
                description = "Successful request",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ImageResponseDto.class))
                )
            )
        }
    )
    @GetMapping
    public List<ImageResponseDto> findAll() {
        logger.info("got all images as dto");
        return imageService.findAllAsDto();
    }

    @Operation(
        description = "Returns a specific image record by unique identifier in url.",
        responses = {
            @ApiResponse(
                description = "Successful request (image found)",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ImageResponseDto.class)
                )
            ),
            @ApiResponse(
                description = "Failed request (image not found)",
                responseCode = "404",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ImageNotFoundException.class)
                )
            )
        }
    )
    @GetMapping("/{id}")
    public ImageResponseDto findById(@PathVariable("id") String id) {
        return imageService.findByIdAsDto(id);
    }


    @Operation(
        description = "Updates a specific image record by unique identifier in url.",
        responses = {
            @ApiResponse(
                description = "Successfully updated (image exists)",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ImageResponseDto.class)
                )
            ),
            @ApiResponse(
                description = "Failed request (image not found)",
                responseCode = "404",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ImageNotFoundException.class)
                )
            )
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ImageResponseDto updateById(
        @PathVariable("id") String id,
        @RequestBody @Valid ImageUpdateDto imageUpdateDto
    ) {
        return imageService.updateById(id, imageUpdateDto);
    }

    @Operation(
        description = "Updates a specific image record by unique identifier in url.",
        responses = {
            @ApiResponse(
                description = "Successfully updated (image exists)",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ImageResponseDto.class)
                )
            ),
            @ApiResponse(
                description = "Failed request (image not found)",
                responseCode = "404",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ImageNotFoundException.class)
                )
            )
        }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable("id") String id) {
        imageService.deleteById(id);
    }
}
