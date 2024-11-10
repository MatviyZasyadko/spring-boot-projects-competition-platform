package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.projects.dto.ProjectCreateDto;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/ui/projects")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectController {

    ProjectService projectService;

    @GetMapping
    public String projectList(Model model) {
        model.addAttribute("allProjects", projectService.findAll());
        return "projects/projects-list";
    }

    @GetMapping("/create")
    public String projectCreate(Model model) {
        if (model.asMap().isEmpty()) {
            model.addAttribute("projectCreateDto", new ProjectCreateDto());
        }
        return "projects/create-project";
    }

    @PostMapping("/create")
    public String projectCreateCallback(
        @Valid @ModelAttribute("projectCreateDto")
        ProjectCreateDto projectCreateDto,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        if (projectCreateDto == null || bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.projectCreateDto",
                bindingResult
            );
            return "redirect:/ui/projects/create";
        }

        projectService.saveFromDto(projectCreateDto, userDetails.getUsername());

        return "redirect:/ui/projects";
    }
}
