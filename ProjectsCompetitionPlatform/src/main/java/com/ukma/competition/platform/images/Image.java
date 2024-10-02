package com.ukma.competition.platform.images;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.projects.Project;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
@Table(name = "images")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Image extends IdentifiableEntity {

    @Column(nullable = false)
    String url;

    @Column(nullable = false)
    String publicId;

    @ManyToMany(mappedBy = "images")
    List<CompetitionEntity> competitionEntities;

    @ManyToMany(mappedBy = "images")
    List<Project> projects;

    @ManyToMany(mappedBy = "images")
    List<Project> users;
}
