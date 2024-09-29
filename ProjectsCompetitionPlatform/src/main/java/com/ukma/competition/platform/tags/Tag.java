package com.ukma.competition.platform.tags;

import com.ukma.competition.platform.competitions.Competition;
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
@Table(name = "tags")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Tag extends IdentifiableEntity {

    @Column(nullable = false, unique = true)
    String name;

    @ManyToMany(mappedBy = "tags")
    List<Project> projects;

    @ManyToMany(mappedBy = "tags")
    List<Competition> competitions;
}
