package com.ukma.competition.platform.competitions;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompetitionServiceImpl extends GenericServiceImpl<Competition, String, CompetitionRepository> implements CompetitionService {

    @Autowired
    public CompetitionServiceImpl(CompetitionRepository repository) {
        super(repository);
    }
}
