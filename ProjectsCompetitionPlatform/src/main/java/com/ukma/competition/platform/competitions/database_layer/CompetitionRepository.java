package com.ukma.competition.platform.competitions.database_layer;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<CompetitionEntity, String>, JpaSpecificationExecutor<CompetitionEntity> {

    Optional<CompetitionEntity> findByName(String name);
}
