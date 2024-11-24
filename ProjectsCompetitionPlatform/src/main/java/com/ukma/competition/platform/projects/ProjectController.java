package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.projects.dto.ProjectCreateDto;
import com.ukma.competition.platform.shared.dto.PaginationDto;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ui/projects")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectController {

    ProjectService projectService;

    @GetMapping
    public String projectList(Model model, @PageableDefault(value = 5) Pageable pageable) {
        Page<ProjectEntity> projectPage = projectService.findAll(null, pageable);
        model.addAttribute("projectsPage", projectService.findAll(null, pageable));
        List<PaginationDto> paginationDtoList = new ArrayList<>();
        if (projectPage.getTotalPages() != 0) {
            IntStream.rangeClosed(1, projectPage.getTotalPages()).forEach(pageNumber -> paginationDtoList.add(
                new PaginationDto(
                    pageNumber,
                    (pageable.getPageNumber() + 1) == pageNumber
                )
            ));
            model.addAttribute(
                "nextPage",
                paginationDtoList.getLast() == null || paginationDtoList.getLast().isActive()
                    ? null
                    : pageable.getPageNumber() + 1);
            model.addAttribute(
                "previousPage",
                paginationDtoList.getFirst() == null || paginationDtoList.getFirst().isActive()
                    ? null
                    : pageable.getPageNumber() - 1
            );
        }
        model.addAttribute("pageNumbers", paginationDtoList);

        return "projects/projects-list";
    }

    @GetMapping("/create")
    public String projectCreate(
        Model model,
        @RequestParam(value = "error", required = false)
        String error
    ) {
        if (model.asMap().isEmpty()) {
            model.addAttribute("projectCreateDto", new ProjectCreateDto());
            if (error != null) {
                model.addAttribute("error", error);
            }
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
    ) {
        if (projectCreateDto == null || bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.projectCreateDto",
                bindingResult
            );
            return "redirect:/ui/projects/create";
        }

        try {
            projectService.saveFromDto(projectCreateDto, userDetails.getUsername());
        } catch (Exception exception) {
            ObjectError error = new ObjectError("globalError", exception.getMessage());
            bindingResult.addError(error);

            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.projectCreateDto",
                bindingResult
            );
            return "redirect:/ui/projects/create";
        }

        return "redirect:/ui/projects";
    }
}
