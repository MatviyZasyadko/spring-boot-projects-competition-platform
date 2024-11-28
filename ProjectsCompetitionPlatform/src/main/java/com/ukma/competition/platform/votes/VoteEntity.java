package com.ukma.competition.platform.votes;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;


@Entity
@Table(name = "votes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class VoteEntity extends IdentifiableEntity {

    @JoinColumn(nullable = false, name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity user;

    @JoinColumn(nullable = false, name = "competition_id")
    @ManyToOne(fetch = FetchType.LAZY)
    CompetitionEntity competition;

    @JoinColumn(nullable = false, name = "project_id")
    @ManyToOne(fetch = FetchType.LAZY)
    ProjectEntity project;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, competition, project);
    }
}
