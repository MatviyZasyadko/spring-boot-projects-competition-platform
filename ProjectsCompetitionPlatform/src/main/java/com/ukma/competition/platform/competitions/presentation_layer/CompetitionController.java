package com.ukma.competition.platform.competitions.presentation_layer;

import com.ukma.competition.platform.competitions.business_layer.CompetitionService;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.projects.ProjectService;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("ui/competitions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CompetitionController {

    CompetitionService competitionService;
    ProjectService projectService;
    UserService userService;

    @GetMapping("/{id}")
    public String getSingleCompetition(Model model, @PathVariable("id") String id) {
//        CompetitionEntity competition = competitionService.findById(id).orElse(null);
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            System.out.println("user is null");
            return "redirect:/ui/competitions";
        }

        CompetitionEntity competition = CompetitionEntity.builder()
                .name("first competition")
                .description("first competition description")
                .beginDate(Instant.now())
                .votingBeginDate(Instant.now())
                .votingEndDate(Instant.now().plus(Duration.ofDays(1)))
                .hasPrizePool(false)
                .priceDescription("price description")
                .prizePool(0.0)
                .creator(user)
                .build();

        if (competition == null) {
            System.out.println("compet is null");
            return "redirect:/ui/competitions";
        }

        model.addAttribute("competition", competition);
        return "competitions/single-competition-page";
    }

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
