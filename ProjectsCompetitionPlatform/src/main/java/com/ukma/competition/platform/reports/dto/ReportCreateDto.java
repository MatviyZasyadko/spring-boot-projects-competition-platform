package com.ukma.competition.platform.reports.dto;

import com.ukma.competition.platform.shared.validations.image.file.ImageFile;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportCreateDto {

    @Length(min = 3, max = 30, message = "Topic value should contain from 3 to 30 characters!")
    String topic;

    @Length(min = 10, max = 250, message = "Report description value should contain from 10 to 250 characters!")
    String description;
}
