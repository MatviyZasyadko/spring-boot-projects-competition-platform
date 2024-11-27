package com.ukma.competition.platform.users;

import com.ukma.competition.platform.auth.oauth.AuthenticationProvider;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.payments.PaymentEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserEntity extends IdentifiableEntity implements UserDetails {

    @Column(nullable = false, unique = true)
    String email;

    @Column
    String fullName;

    @Column
    String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    UserRole userRole = UserRole.USER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AuthenticationProvider authenticationProvider;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<PaymentEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ProjectEntity> projects = new ArrayList<>();

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<CompetitionEntity> competitions = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
        name = "images_users",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    @Builder.Default
    List<ImageEntity> images = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.userRole);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public void addImage(ImageEntity image) {
        this.images.add(image);
        image.getUsers().add(this);
    }

    public void addProject(ProjectEntity project) {
        this.projects.add(project);
        project.setCreator(this);
    }

    public ImageEntity getLogo() {
        return images.stream().filter(ImageEntity::getMain).findFirst()
            .orElse(null);
    }

    public String getLogoUrl() {
        return Optional.ofNullable(this.getLogo())
            .map(ImageEntity::getUrl)
            .orElse(null);
    }
}
