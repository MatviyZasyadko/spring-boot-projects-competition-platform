package com.ukma.competition.platform.competitions.presentation_layer;

import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.projects.dto.ProjectListItemDto;
import com.ukma.competition.platform.shared.dto.BaseDto;
import com.ukma.competition.platform.shared.validations.WithinTwoYears;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CompetitionItemDto extends BaseDto {

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description can be up to 1000 characters")
    private String description;

    @NotNull(message = "Begin date is required")
    @PastOrPresent(message = "Begin date must be in the past or present")
    private Instant beginDate;

    @NotNull(message = "Voting begin date is required")
    @WithinTwoYears(message = "Voting begin date cannot be more than 2 years in the future")
    private Instant votingBeginDate;

    @NotNull(message = "Voting end date is required")
    @WithinTwoYears(message = "Voting end date cannot be more than 2 years in the future")
    private Instant votingEndDate;

    @NotNull(message = "hasPrizePool flag is required")
    private Boolean hasPrizePool;

    @Size(max = 255, message = "Price description can be up to 255 characters")
    private String priceDescription;

    @Min(value = 0, message = "Prize pool must be greater than or equal to 0")
    private Double prizePool;

    private List<ImageEntity> images;

    private List<ProjectListItemDto> projects;
}
