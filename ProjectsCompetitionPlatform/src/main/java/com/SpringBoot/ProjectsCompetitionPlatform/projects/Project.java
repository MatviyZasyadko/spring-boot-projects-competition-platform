package com.SpringBoot.ProjectsCompetitionPlatform.projects;

import com.SpringBoot.ProjectsCompetitionPlatform.competitions.Competition;
import com.SpringBoot.ProjectsCompetitionPlatform.users.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @ManyToMany(mappedBy = "projects")
    List<Competition> competitions;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
}


