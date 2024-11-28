package com.ukma.competition.platform.reports;

import com.ukma.competition.platform.shared.IdentifiableEntity;
import com.ukma.competition.platform.users.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "reports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReportEntity extends IdentifiableEntity {

    @Column(nullable = false)
    String topic;

    @Column(nullable = false)
    String description;

    @Column
    String adminComment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ReportStatus reportStatus;

    @Column
    Instant approveDate;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    UserEntity user;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, description, adminComment, reportStatus, approveDate, user);
    }
}
