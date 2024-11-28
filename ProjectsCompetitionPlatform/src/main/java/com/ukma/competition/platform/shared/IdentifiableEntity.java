package com.ukma.competition.platform.shared;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @CreationTimestamp
    Instant createdAt;

    @UpdateTimestamp
    Instant updatedAt;

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IdentifiableEntity && equalsHelper((IdentifiableEntity) obj, this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt);
    }

    private boolean equalsHelper(IdentifiableEntity i1, IdentifiableEntity i2) {
        if (i1 == i2) {
            return true;
        } else if (i1 == null || !(Hibernate.getClass(i1).equals(Hibernate.getClass(i2)))) {
            return false;
        } else {
            IdentifiableEntity other = i1;
            if (other.getId() == null) {
                return false;
            } else {
                return Objects.equals(i2.getId(), other.getId());
            }
        }
    }

}
