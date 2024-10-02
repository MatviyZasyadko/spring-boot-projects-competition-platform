package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.images.Image;
import com.ukma.competition.platform.payments.Payment;
import com.ukma.competition.platform.projects.Project;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.tags.Tag;
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
    List<Image> images;
    List<Project> projects;
    List<Tag> tags;
    List<Payment> payments;
}
