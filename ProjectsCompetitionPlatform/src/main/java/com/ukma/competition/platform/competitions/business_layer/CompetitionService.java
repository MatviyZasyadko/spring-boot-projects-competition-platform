package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.shared.GenericService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface CompetitionService extends GenericService<Competition, String> {

    public Logger logger = LogManager.getLogger(CompetitionServiceImpl.class);
    Competition updateById(String id, Competition entity) throws EntityNotFoundException;
}
