package com.ukma.competition.platform.projects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ukma.competition.platform.comments.dto.CommentCreateDto;
import com.ukma.competition.platform.projects.dto.ProjectCreateUpdateDto;
import com.ukma.competition.platform.projects.dto.ProjectListDto;
import com.ukma.competition.platform.projects.dto.ProjectListItemDto;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui/projects")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectController {

    ProjectService projectService;

    @GetMapping
    public String getProjectList(Model model, @PageableDefault(value = 5) Pageable pageable, @RequestParam(value = "search", required = false) String search) {
        ProjectListDto projectPage = projectService.findAllWithSearch(pageable, search);
        if (projectPage.getTotalPages() != 0) {
            model.addAttribute(
                    "nextPage",
                    projectPage.getPaginationDtoList().getLast() == null || projectPage.getPaginationDtoList().getLast().isActive()
                            ? null
                            : pageable.getPageNumber() + 2);
            model.addAttribute(
                    "previousPage",
                    projectPage.getPaginationDtoList().getFirst() == null || projectPage.getPaginationDtoList().getFirst().isActive()
                            ? null
                            : pageable.getPageNumber()
            );
        }
        model.addAttribute("pageNumbers", projectPage.getPaginationDtoList());
        model.addAttribute("projectsPage", projectPage);

        return "projects/projects-list";
    }

    @GetMapping("/{id}")
    public String getSingleProject(Model model, @PathVariable("id") String id) {
        model.addAttribute("project", projectService.findOneAsDtoById(id));
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setProjectId(id);
        model.addAttribute("commentCreateDto", commentCreateDto);
        return "projects/single-project-page";
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSingleProject(@PathVariable("id") String id) {
        projectService.deleteById(id);
    }

    @PostMapping("/add-comment")
    public String commentCreateCallback(
            @Valid @ModelAttribute("commentCreateDto")
            CommentCreateDto commentCreateDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        boolean added = projectService.addComment(commentCreateDto);

        if (added) {
            redirectAttributes.addFlashAttribute("message", "Comment added successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to add comment. Please try again.");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/ui/projects/" + commentCreateDto.getProjectId();
    }

    @GetMapping("/create")
    public String projectCreate(
            Model model,
            @RequestParam(value = "error", required = false)
            String error
    ) {
        if (model.asMap().isEmpty()) {
            model.addAttribute("projectCreateUpdateDto", new ProjectCreateUpdateDto());
        }
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "projects/create-project";
    }

    @PostMapping("/create")
    public String projectCreateCallback(
            @Valid @ModelAttribute("projectCreateUpdateDto")
            ProjectCreateUpdateDto projectCreateUpdateDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (projectCreateUpdateDto == null || bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.projectCreateUpdateDto",
                    bindingResult
            );
            return "redirect:/ui/projects/create";
        }

        try {
            if (projectCreateUpdateDto.getImages() == null || projectCreateUpdateDto.getImages().isEmpty()) {
                ObjectError error = new ObjectError("globalError", "You should provide at least one image for project");
                bindingResult.addError(error);

                redirectAttributes.addFlashAttribute(
                        "org.springframework.validation.BindingResult.projectCreateUpdateDto",
                        bindingResult
                );
                return "redirect:/ui/projects/create";
            }
            projectService.saveFromDto(projectCreateUpdateDto, userDetails.getUsername());
        } catch (Exception exception) {
            ObjectError error = new ObjectError("globalError", exception.getMessage());
            bindingResult.addError(error);

            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.projectCreateUpdateDto",
                    bindingResult
            );
            return "redirect:/ui/projects/create";
        }

        return "redirect:/ui/projects";
    }

    @GetMapping("/update/{id}")
    public String projectUpdate(
            Model model,
            @PathVariable("id")
            String id,
            @RequestParam(value = "error", required = false)
            String error
    ) throws JsonProcessingException {
        if (model.asMap().isEmpty()) {
            model.addAttribute("projectCreateUpdateDto", projectService.buildUpdateDto(id));
            if (error != null) {
                model.addAttribute("error", error);
            }
        }
        return "projects/create-project";
    }

    @PostMapping("/update/{id}")
    public String projectUpdateCallback(
            @ModelAttribute("projectCreateUpdateDto") @Valid
            ProjectCreateUpdateDto projectCreateUpdateDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @PathVariable("id") String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (projectCreateUpdateDto == null || bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.projectCreateUpdateDto",
                    bindingResult
            );
            return "redirect:/ui/projects/update/" + id;
        }

        try {
            projectService.saveFromDto(projectCreateUpdateDto, userDetails.getUsername());
        } catch (Exception exception) {
            ObjectError error = new ObjectError("globalError", exception.getMessage());
            bindingResult.addError(error);

            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.projectCreateUpdateDto",
                    bindingResult
            );
            return "redirect:/ui/projects/update/" + id;
        }

        return "redirect:/ui/projects/" + id;
    }
}
