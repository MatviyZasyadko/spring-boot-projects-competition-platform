package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.comments.CommentEntity;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.tags.TagEntity;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.votes.VoteEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "projects")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProjectEntity extends IdentifiableEntity {

    @Column(nullable = false, length = 30)
    String name;

    @Column(nullable = false, length = 250)
    String shortDescription;

    @Column(nullable = false, length = 500)
    String fullDescription;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity creator;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<CommentEntity> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "projects")
    @Builder.Default
    List<CompetitionEntity> competitions = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "images_projects",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    @Builder.Default
    List<ImageEntity> images = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "tags_competitions",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    List<TagEntity> tags = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<VoteEntity> votes = new ArrayList<>();

    public ImageEntity getLogo() {
        return images.stream().filter(ImageEntity::getMain).findFirst()
            .orElse(null);
    }

    public String getLogoUrl() {
        return this.getLogo() == null ? null : this.getLogo().getUrl();
    }

    public void addImage(ImageEntity image) {
        this.images.add(image);
        image.getProjects().add(this);
    }

    public void removeImage(ImageEntity image) {
        this.images.remove(image);
        image.getProjects().remove(this);
    }
}


