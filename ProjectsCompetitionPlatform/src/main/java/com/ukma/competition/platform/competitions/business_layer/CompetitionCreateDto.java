package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.shared.validations.image.file.ImageFile;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompetitionCreateDto {

    @Length(min = 3, max = 30, message = "Name value should contain from 3 to 30 characters!")
    String name;

    @Length(min = 10, max = 250, message = "Description value should contain from 10 to 250 characters!")
    String description;

    @ImageFile
    MultipartFile logo;

    @NotNull
    @Future(message = "Competition should end in the future!")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime endDate;
}