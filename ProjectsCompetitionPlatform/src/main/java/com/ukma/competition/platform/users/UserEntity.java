package com.ukma.competition.platform.users;

import com.ukma.competition.platform.images.Image;
import com.ukma.competition.platform.payments.Payment;
import com.ukma.competition.platform.projects.Project;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserEntity extends IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String fullName;

    @Column(nullable = false)
    String userName;

    @Column(nullable = false)
    String password;

    @OneToMany(mappedBy = "user")
    List<Payment> payments;

    @OneToMany(mappedBy = "user")
    List<Project> projects;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "images_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    List<Image> images;
}