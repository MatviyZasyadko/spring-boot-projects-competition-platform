package com.ukma.competition.platform.images.dto;

import com.ukma.competition.platform.shared.dto.BaseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageResponseDto extends BaseDto {

    String url;
    String publicId;

    @Builder
    public ImageResponseDto(
        String id,
        Instant createdAt,
        Instant updatedAt,
        String url,
        String publicId
    ) {
        super(id, createdAt, updatedAt);
        this.url = url;
        this.publicId = publicId;
    }
}
