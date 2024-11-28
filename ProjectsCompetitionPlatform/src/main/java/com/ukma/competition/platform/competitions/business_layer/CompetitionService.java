package com.ukma.competition.platform.competitions.business_layer;

import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.competitions.presentation_layer.CompetitionItemDto;
import com.ukma.competition.platform.competitions.presentation_layer.ProjectApplyToCompetitionDto;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.shared.GenericService;
import com.ukma.competition.platform.users.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public interface CompetitionService extends GenericService<CompetitionEntity, String> {
    Logger logger = LogManager.getLogger(CompetitionServiceImpl.class);

    void saveFromDto(CompetitionCreateDto competitionCreateDto, String userEmail) throws Exception;

    List<CompetitionItemDto> findAllAsDto();

    CompetitionItemDto findByIdAsDto(String id);

    void applyProjectToCompetition(ProjectApplyToCompetitionDto projectApplyToCompetitionDto, String competitionId);

    String findProjectIdWithVoteFromUser(String competitionId, UserEntity user);

    boolean addOrChangeVote(CompetitionEntity competition, ProjectEntity project, UserEntity user);
}
