package com.ukma.competition.platform.payments;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Entity
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaymentEntity extends IdentifiableEntity {

    @Column(nullable = false)
    @Builder.Default
    Double sum = 0.;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    @Column
    String failReason;

    @JoinColumn(name = "competition_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    CompetitionEntity competition;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity user;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum, paymentStatus, failReason, competition, user);
    }
}
