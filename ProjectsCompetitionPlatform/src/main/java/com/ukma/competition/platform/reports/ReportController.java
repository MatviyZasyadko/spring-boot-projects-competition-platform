package com.ukma.competition.platform.reports;

import com.ukma.competition.platform.reports.dto.ReportCreateDto;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;

@Controller
@RequestMapping("/ui/reports")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReportController {

    ReportService reportService;

    UserService userService;

    @GetMapping
    public String reportList(Model model) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByEmail(principal.getName()).orElse(null);
        model.addAttribute("userReports", reportService.findAllByUser(user));
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
            RedirectAttributes redirectAttributes
    ) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (reportCreateDto == null || bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.reportCreateDto",
                    bindingResult
            );
            return "redirect:/ui/reports/create";
        }

        try {
            reportService.saveFromDto(reportCreateDto, token.getName());
        } catch (Exception exception) {
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

    @PostMapping("/approve-reopen-report")
    public String approveOrReopenReport(@RequestParam("reportId") String reportId,
                                      @RequestParam("action") String action,
                                      @RequestParam(value = "comment", required = false) String comment,
                                      Model model) {
        ReportEntity report = reportService.findById(reportId).orElse(null);

        if (report == null) {
            return "redirect:/ui/admin-page";
        }

        if ("approve".equals(action)) {
            report.setReportStatus(ReportStatus.APPROVED);
            report.setApproveDate(Instant.now());
            report.setAdminComment(comment);
        } else if ("reopen".equals(action)) {
            report.setReportStatus(ReportStatus.IN_PROCESS);
            report.setApproveDate(null);
            report.setAdminComment(comment);
        }

        reportService.save(report);

        model.addAttribute("allReports", reportService.findAll());
        return "redirect:/ui/admin-page";
    }
}
