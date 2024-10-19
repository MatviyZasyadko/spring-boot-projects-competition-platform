package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.tags.TagEntity;
import com.ukma.competition.platform.users.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "projects")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProjectEntity extends IdentifiableEntity {

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity user;

    @ManyToMany(mappedBy = "projects")
    List<CompetitionEntity> competitionEntities;

    @ManyToMany
    @JoinTable(
        name = "images_projects",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    List<ImageEntity> images;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "tags_competitions",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<TagEntity> tags;
}


