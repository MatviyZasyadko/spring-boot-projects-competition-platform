package com.ukma.competition.platform.competitions.presentation_layer;

import com.ukma.competition.platform.competitions.business_layer.CompetitionCreateDto;
import com.ukma.competition.platform.competitions.business_layer.CompetitionService;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.projects.ProjectEntity;
import com.ukma.competition.platform.projects.ProjectService;
import com.ukma.competition.platform.projects.dto.SelectedProjectDto;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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


@Controller
@RequestMapping("ui/competitions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CompetitionController {

    CompetitionService competitionService;
    ProjectService projectService;
    UserService userService;

    static final String ERROR = "error";
    static final String REDIRECT_UI_COMPETITIONS = "redirect:/ui/competitions/";

    @GetMapping
    public String findAllAsDto(Model model) {
        model.addAttribute("competitions", competitionService.findAllAsDto());

        return "competitions/competitions-list";
    }

    @GetMapping("/{id}")
    public String singleCompetitionPage(
        Model model,
        @PathVariable("id") String competitionId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        model.addAttribute("competition", competitionService.findByIdAsDto(competitionId));

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByEmail(principal.getName()).orElse(null);

        model.addAttribute("usersProjects", projectService.findAll(
            ((root, query, cb) -> cb.equal(root.get("creator").get("email"), userDetails.getUsername())),
            PageRequest.of(0, Integer.MAX_VALUE)
        ));

        model.addAttribute("projectApplyToCompetitionDto", new ProjectApplyToCompetitionDto());

        String projectId = competitionService.findProjectIdWithVoteFromUser(competitionId, user);
        model.addAttribute("selectedProjectDto", new SelectedProjectDto(projectId));

        return "competitions/single-competition-page";
    }

    @PostMapping("/{competitionId}/vote")
    public String submitVote(@PathVariable("competitionId") String competitionId,
                             @Valid @ModelAttribute("selectedProjectDto") SelectedProjectDto selectedProjectDto,
                             RedirectAttributes redirectAttributes) {
        CompetitionEntity competition = competitionService.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));

        ProjectEntity project = projectService.findById(selectedProjectDto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByEmail(principal.getName()).orElse(null);

        boolean voteAddedOrChanged = competitionService.addOrChangeVote(competition, project, user);

        if (voteAddedOrChanged) {
            redirectAttributes.addFlashAttribute("message", "Your vote has been submitted!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to submit vote. Please try again.");
            redirectAttributes.addFlashAttribute("messageType", ERROR);
        }

        return REDIRECT_UI_COMPETITIONS + competitionId;
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
            model.addAttribute(ERROR, error);
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

    @PostMapping("/{id}/apply")
    public String applyProject(
        @ModelAttribute("projectApplyToCompetitionDto")
        ProjectApplyToCompetitionDto projectApplyToCompetitionDto,
        @PathVariable("id") String competitionId,
        RedirectAttributes redirectAttributes
    ) {
        try {
            competitionService.applyProjectToCompetition(projectApplyToCompetitionDto, competitionId);
            return REDIRECT_UI_COMPETITIONS + competitionId;
        } catch (Exception exception) {
            redirectAttributes.addAttribute(ERROR, "Error occured while applying project to a competition: " + exception.getMessage());
            return REDIRECT_UI_COMPETITIONS + competitionId;
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") String id) {
        competitionService.deleteById(id);

        return "redirect:/ui/competitions";
    }

}
