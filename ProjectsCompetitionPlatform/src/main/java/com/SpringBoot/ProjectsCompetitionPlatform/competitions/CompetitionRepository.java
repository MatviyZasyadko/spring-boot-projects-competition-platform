package com.SpringBoot.ProjectsCompetitionPlatform.competitions;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompetitionRepository {

    List<Competition> competitions = new ArrayList<>(List.of(
        new Competition(
            "competition 1",
            "competition 1!!!",
            new ArrayList<>(),
            new Date(),
            2000
        ),
        new Competition(
            "another name",
            "Second competition",
            new ArrayList<>(),
            new Date(),
            3000
        ),
        new Competition(
            "one more name",
            "third competition",
            new ArrayList<>(),
            new Date(),
            3000
        )
    ));

    public boolean create(Competition competition) {
        return competitions.add(competition);
    }

    public List<Competition> getAll() {
        return competitions;
    }

    public Competition getOneByName(String name) {
        return competitions.stream()
            .filter(competition -> competition.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    public Competition updateByName(Competition newCompetition, String name) {
        Competition competition = getOneByName(name);
        if (competition != null) {
            competition.setName(newCompetition.getName());
            competition.setDescription(newCompetition.getDescription());
            competition.setBeginDate(newCompetition.getBeginDate());
            competition.setProjects(newCompetition.getProjects());
            competition.setPrizePool(newCompetition.getPrizePool());
        }

        return competition;
    }

    public boolean deleteByName(String name) {
        Competition competition = getOneByName(name);
        return competitions.remove(competition);
    }
}
