package com.ukma.competition.platform.users;

import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.payments.PaymentEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserRole userRole;

    @OneToMany(mappedBy = "user")
    List<PaymentEntity> payments;

    @OneToMany(mappedBy = "user")
    List<ProjectEntity> projects;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
        name = "images_users",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    List<ImageEntity> images;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.userRole);
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
