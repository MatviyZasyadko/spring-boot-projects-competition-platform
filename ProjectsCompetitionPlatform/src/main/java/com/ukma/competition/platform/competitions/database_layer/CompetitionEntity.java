package com.ukma.competition.platform.competitions.database_layer;

import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.payments.PaymentEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.tags.TagEntity;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.votes.VoteEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    Instant votingEndDate;

    @JoinColumn(name = "organizer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity organizer;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "images_competitions",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    @Builder.Default
    List<ImageEntity> images = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "projects_competitions",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @Builder.Default
    List<ProjectEntity> projects = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "tags_competitions",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    List<TagEntity> tags = new ArrayList<>();

    @OneToMany(mappedBy = "competition")
    @Builder.Default
    List<PaymentEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<VoteEntity> votes = new ArrayList<>();

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

    public void addProject(ProjectEntity project) {
        this.getProjects().add(project);
        project.getCompetitions().add(this);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, votingEndDate, organizer, images, projects, tags, payments, votes);
    }
}