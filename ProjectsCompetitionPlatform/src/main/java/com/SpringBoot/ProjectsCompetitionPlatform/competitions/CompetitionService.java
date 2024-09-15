package com.SpringBoot.ProjectsCompetitionPlatform.competitions;

import java.util.List;

public interface CompetitionService {

    boolean create(Competition competition);
    List<Competition> getAll();
    Competition getOneByName(String name);
    Competition updateByName(String name, Competition competition);
    boolean deleteByName(String name);
}
