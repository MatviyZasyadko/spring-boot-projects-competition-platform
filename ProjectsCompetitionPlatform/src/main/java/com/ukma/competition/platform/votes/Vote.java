package com.ukma.competition.platform.votes;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.projects.Project;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.users.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "votes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Vote extends IdentifiableEntity {

    @JoinColumn(nullable = false, name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity user;

    @JoinColumn(nullable = false, name = "competition_id")
    @ManyToOne(fetch = FetchType.LAZY)
    CompetitionEntity competitionEntity;

    @JoinColumn(nullable = false, name = "project_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Project project;
}
