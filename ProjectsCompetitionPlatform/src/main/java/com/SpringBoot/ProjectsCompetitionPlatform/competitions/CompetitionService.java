package com.SpringBoot.ProjectsCompetitionPlatform.competitions;

import java.util.List;

public interface CompetitionService {

    void save(Competition competition);
    List<Competition> findAll();
    Competition findOneById(String id);
    Competition updateOneById(String name, Competition competition);
    void deleteById(String name);
}
