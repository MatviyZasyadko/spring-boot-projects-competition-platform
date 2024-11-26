package com.ukma.competition.platform.images;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.users.UserEntity;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "images")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ImageEntity extends IdentifiableEntity {

    @Column(nullable = false)
    String url;

    @Column(nullable = false)
    @Builder.Default
    String name = "image.jpg";

    @Column
    String publicId;

    @Column(nullable = false)
    @Builder.Default
    Boolean main = Boolean.FALSE;

    @ManyToMany(mappedBy = "images")
    @Builder.Default
    List<CompetitionEntity> competitions = new ArrayList<>();

    @ManyToMany(mappedBy = "images")
    @Builder.Default
    List<ProjectEntity> projects = new ArrayList<>();

    @ManyToMany(mappedBy = "images")
    @Builder.Default
    List<UserEntity> users = new ArrayList<>();

    public boolean isFromApplicationCloudStorage() {
        return this.publicId != null;
    }
}
