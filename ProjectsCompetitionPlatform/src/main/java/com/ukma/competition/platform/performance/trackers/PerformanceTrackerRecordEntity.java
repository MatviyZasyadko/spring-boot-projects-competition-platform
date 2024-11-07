package com.ukma.competition.platform.performance.trackers;

import com.ukma.competition.platform.shared.IdentifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "performance_tracker_records")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PerformanceTrackerRecordEntity extends IdentifiableEntity {

    @Column(nullable = false)
    Double processTime;

    @Column(nullable = false)
    String methodName;

    @Column(nullable = false)
    String className;

    @Column(nullable = false)
    Boolean isSuccessful;
}
