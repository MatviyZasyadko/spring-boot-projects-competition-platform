package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.payments.PaymentEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.tags.TagEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Competition extends IdentifiableEntity {
    String name;
    String description;
    Instant beginDate;
    Instant votingBeginDate;
    Instant votingEndDate;
    Boolean hasPrizePool;
    String priceDescription;
    Double prizePool;
    List<ImageEntity> images;
    List<ProjectEntity> projects;
    List<TagEntity> tags;
    List<PaymentEntity> payments;
}
