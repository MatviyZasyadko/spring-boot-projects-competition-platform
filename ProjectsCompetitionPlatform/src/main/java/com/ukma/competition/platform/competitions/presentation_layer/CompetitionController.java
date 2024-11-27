package com.ukma.competition.platform.competitions.presentation_layer;

import com.ukma.competition.platform.competitions.business_layer.CompetitionService;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.projects.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("ui/competitions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CompetitionController {

    CompetitionService competitionService;
    ProjectService projectService;

    //
    //  @PostMapping("/upload")
    //  public ResponseEntity<CompetitionDto> createNewCompetition(@Valid @RequestBody CompetitionDto newCompetitionDto) {
    //      Competition newCompetition = convertDtoToCompetition(newCompetitionDto);
    //      Competition savedCompetition = competitionService.save(newCompetition);
    //      CompetitionDto savedCompetitionDto = convertCompetitionToDto(savedCompetition);
    //      return ResponseEntity.status(HttpStatus.CREATED).body(savedCompetitionDto);
    //  }
//
    @GetMapping
    public String findAll(Model model) {
        competitionService.logger.info("Received request to retrieve all competitions");
        ThreadContext.put("methodName", "findAll");
        ThreadContext.put("id", "1");
        List<CompetitionEntity> allCompetitions = competitionService.findAll();
        //List<CompetitionItemDto> allCompetitionDtos = allCompetitions.stream()
        //    .map(this::convertCompetitionToDto)
        //    .toList();
        ThreadContext.clearAll();
        return "competitions/competitions-list";
    }

    //
    //  @GetMapping("/{id}")
    //  public ResponseEntity<CompetitionDto> findById(@PathVariable("id") String id) {
    //      Optional<CompetitionEntity> optionalCompetition = competitionService.findById(id);
//
    //      competitionService.logger.info("Received request to retrieve competition with ID: {}", id);
//
    //      ThreadContext.put("competitionID", id);
//
    //      if (optionalCompetition.isPresent()) {
    //          CompetitionDto competitionDto = convertCompetitionToDto(optionalCompetition.get());
    //          return ResponseEntity.ok(competitionDto);
    //      } else {
    //          return ResponseEntity.notFound().build();
    //      }
    //  }
//
    //  @PutMapping("/{id}")
    //  public ResponseEntity<CompetitionDto> updateById(
    //          @PathVariable("id") String id,
    //          @RequestBody @Valid CompetitionDto competitionDto
    //  ) {
//
    //      competitionService.logger.info("Received request to update competition with ID: {}", id);
    //      ThreadContext.put("competitionID", id);
    //      ThreadContext.put("competitionName", competitionDto.getName());
//
    //      Competition competition = convertDtoToCompetition(competitionDto);
    //      try {
    //          Competition updatedCompetition = competitionService.updateById(id, competition);
    //          CompetitionDto updatedCompetitionDto = convertCompetitionToDto(updatedCompetition);
    //          ThreadContext.clearAll();
    //          return ResponseEntity.ok(updatedCompetitionDto);
    //      } catch (EntityNotFoundException e) {
    //          competitionService.logger.error("Failed to update competition. Competition with ID: {} not found", id);
    //          ThreadContext.clearAll();
    //          return ResponseEntity.notFound().build();
    //      }
    //  }
//
    //  @DeleteMapping("/{id}")
    //  @ResponseStatus(HttpStatus.NO_CONTENT)
    //  public void deleteById(@PathVariable("id") String id) {
    //      competitionService.deleteById(id);
    //  }
//
//
    //  private Competition convertDtoToCompetition(CompetitionDto dto) {
    //      Competition competition = new Competition();
    //      competition.setName(dto.getName());
    //      competition.setDescription(dto.getDescription());
    //      competition.setBeginDate(dto.getBeginDate());
    //      competition.setVotingBeginDate(dto.getVotingBeginDate());
    //      competition.setVotingEndDate(dto.getVotingEndDate());
    //      competition.setHasPrizePool(dto.getHasPrizePool());
    //      competition.setPriceDescription(dto.getPriceDescription());
    //      competition.setPrizePool(dto.getPrizePool());
    //      competition.setImages(dto.getImages());
    //      competition.setProjects(dto.getProjects());
    //      competition.setTags(dto.getTags());
    //      competition.setPayments(dto.getPayments());
    //      return competition;
    //  }
//
  //  private CompetitionItemDto convertCompetitionToDto(CompetitionEntity competition) {
  //      CompetitionItemDto dto = new CompetitionItemDto();
  //      dto.setId(competition.getId());
  //      dto.setName(competition.getName());
  //      dto.setDescription(competition.getDescription());
  //      dto.setBeginDate(competition.getBeginDate());
  //      dto.setVotingBeginDate(competition.getVotingBeginDate());
  //      dto.setVotingEndDate(competition.getVotingEndDate());
  //      dto.setHasPrizePool(competition.getHasPrizePool());
  //      dto.setPriceDescription(competition.getPriceDescription());
  //      dto.setPrizePool(competition.getPrizePool());
  //      dto.setImages(competition.getImages());
  //      dto.setProjects(competition.getProjects());
  //      dto.setTags(competition.getTags());
  //      dto.setPayments(competition.getPayments());
  //      return dto;
  //  }
}
