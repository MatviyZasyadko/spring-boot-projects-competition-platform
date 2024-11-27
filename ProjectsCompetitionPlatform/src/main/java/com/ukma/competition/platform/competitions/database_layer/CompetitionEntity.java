package com.ukma.competition.platform.competitions.database_layer;

import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.payments.PaymentEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.tags.TagEntity;
import com.ukma.competition.platform.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "competitions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CompetitionEntity extends IdentifiableEntity {

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Instant beginDate;

    @Column(nullable = false)
    Instant votingBeginDate;

    @Column(nullable = false)
    Instant votingEndDate;

    @Column(nullable = false)
    Boolean hasPrizePool;

    @Column(nullable = false)
    String priceDescription;

    @JoinColumn(name = "creator_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity creator;

    @Column
    Double prizePool;

    @ManyToMany
    @JoinTable(
        name = "images_competitions",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    List<ImageEntity> images;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "projects_competitions",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    List<ProjectEntity> projects;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "tags_competitions",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<TagEntity> tags;

    @OneToMany(mappedBy = "competition")
    List<PaymentEntity> payments;

    public ImageEntity getLogo() {
        return images.stream().filter(ImageEntity::getMain).findFirst()
            .orElse(null);
    }

    public String getLogoUrl() {
        return Optional.ofNullable(this.getLogo())
            .map(ImageEntity::getUrl)
            .orElse(null);
    }

    public void addImage(ImageEntity image) {
        this.images.add(image);
        image.getCompetitions().add(this);
    }
}