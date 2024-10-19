package com.ukma.competition.platform.users;

import com.ukma.competition.platform.images.ImageEntity;
import com.ukma.competition.platform.payments.PaymentEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import lombok.AccessLevel;
import lombok.Getter;
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

    List<PaymentEntity> payments;

    List<ProjectEntity> projects;

    List<ImageEntity> images;
}