package com.ukma.competition.platform.users;

import com.ukma.competition.platform.images.Image;
import com.ukma.competition.platform.payments.Payment;
import com.ukma.competition.platform.projects.Project;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter

public class User extends IdentifiableEntity {

    String email;

    String fullName;

    String password;

    List<Payment> payments;

    List<Project> projects;

    List<Image> images;
}