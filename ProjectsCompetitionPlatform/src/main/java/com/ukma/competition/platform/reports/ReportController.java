package com.ukma.competition.platform.reports;

import com.ukma.competition.platform.reports.dto.ReportCreateDto;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui/reports")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReportController {

    ReportService reportService;

    @GetMapping
    public String reportList(Model model) {
        model.addAttribute("allReports", reportService.findAll());
        return "reports/reports-list";
    }

    @GetMapping("/create")
    public String reportCreate(Model model) {
        if (model.asMap().isEmpty()) {
            model.addAttribute("reportCreateDto", new ReportCreateDto());
        }
        return "reports/create-report";
    }

    @PostMapping("/create")
    public String reportCreateCallback(
            @Valid @ModelAttribute("reportCreateDto")
            ReportCreateDto reportCreateDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (reportCreateDto == null || bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.reportCreateDto",
                    bindingResult
            );
            return "redirect:/ui/reports/create";
        }

        try {
            reportService.saveFromDto(reportCreateDto, userDetails.getUsername());
            System.out.println("save from dto");
        } catch (Exception exception) {
            System.out.println("caught error");
            ObjectError error = new ObjectError("globalError", exception.getMessage());
            bindingResult.addError(error);

            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.reportCreateDto",
                    bindingResult
            );
            return "redirect:/ui/reports/create";
        }

        return "redirect:/ui/reports";
    }
}
