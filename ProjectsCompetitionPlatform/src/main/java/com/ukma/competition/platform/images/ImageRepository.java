package com.ukma.competition.platform.images;

import com.ukma.competition.platform.votes.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String>, JpaSpecificationExecutor<ImageEntity> {
}
