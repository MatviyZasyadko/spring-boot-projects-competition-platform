package com.ukma.competition.platform.competitions.presentation_layer;

import com.ukma.competition.platform.competitions.business_layer.CompetitionCreateDto;
import com.ukma.competition.platform.competitions.business_layer.CompetitionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


@Controller
@RequestMapping("ui/competitions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CompetitionController {

    CompetitionService competitionService;

    @GetMapping
    public String findAllAsDto(Model model) {
        model.addAttribute("competitions", competitionService.findAllAsDto());

        return "competitions/competitions-list";
    }

    @GetMapping("/create")
    public String competitionCreate(
        Model model,
        @RequestParam(value = "error", required = false)
        String error
    ) {
        if (model.asMap().isEmpty()) {
            model.addAttribute("competitionCreateDto", new CompetitionCreateDto());
        }
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "competitions/create-competition";
    }

    @PostMapping("/create")
    public String competitionCreateCallback(
        @Valid @ModelAttribute("competitionCreateDto")
        CompetitionCreateDto competitionCreateDto,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (competitionCreateDto == null || bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.competitionCreateDto",
                bindingResult
            );
            return "redirect:/ui/competitions/create";
        }

        try {
            competitionService.saveFromDto(competitionCreateDto, userDetails.getUsername());
        } catch (Exception exception) {
            ObjectError error = new ObjectError("globalError", exception.getMessage());
            bindingResult.addError(error);

            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.competitionCreateDto",
                bindingResult
            );
            return "redirect:/ui/competitions/create";
        }

        return "redirect:/ui/competitions";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
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
