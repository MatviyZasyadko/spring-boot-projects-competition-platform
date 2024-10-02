package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.shared.GenericService;
import jakarta.persistence.EntityNotFoundException;

public interface CompetitionService extends GenericService<Competition, String> {
    Competition updateById(String id, Competition entity) throws EntityNotFoundException;
}
