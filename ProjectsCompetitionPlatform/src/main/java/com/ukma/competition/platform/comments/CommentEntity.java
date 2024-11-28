package com.ukma.competition.platform.comments;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CommentEntity extends IdentifiableEntity {
    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity author;

    @Column(nullable = false, length = 250)
    String text;

    @JoinColumn(name = "project_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    ProjectEntity project;
}
