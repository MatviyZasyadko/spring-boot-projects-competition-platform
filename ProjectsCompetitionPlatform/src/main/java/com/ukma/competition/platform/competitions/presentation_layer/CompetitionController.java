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

    @GetMapping("/{id}")
    public String singleCompetitionPage(Model model, @PathVariable("id") String id) {
        model.addAttribute("competition", competitionService.findByIdAsDto(id));
        return "competitions/single-competition-page";
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

    //  @DeleteMapping("/{id}")
    //  @ResponseStatus(HttpStatus.NO_CONTENT)
    //  public void deleteById(@PathVariable("id") String id) {
    //      competitionService.deleteById(id);
    //  }
//
//
}
