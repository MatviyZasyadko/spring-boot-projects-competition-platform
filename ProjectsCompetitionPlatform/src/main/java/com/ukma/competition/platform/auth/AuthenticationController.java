package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.auth.dto.LoginRequestDto;
import com.ukma.competition.platform.auth.dto.RegistrationRequestDto;
import com.ukma.competition.platform.reports.ReportService;
import com.ukma.competition.platform.shared.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Controller
@RequestMapping("/ui")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationController {

    AuthenticationService authenticationService;
    ReportService reportService;

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        if (model.asMap().isEmpty()) {
            model.addAttribute("authDto", new RegistrationRequestDto());
        }
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registrationCallback(
        @Valid @ModelAttribute("authDto")
        RegistrationRequestDto authDto,
        BindingResult bindingResult,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes
    ) {
        if (authDto == null || bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.authDto",
                bindingResult
            );
            return "redirect:/ui/registration";
        }
        try {
            response.addCookie(authenticationService.register(authDto));
            return "redirect:/ui/main";
        } catch (AuthenticationException exception) {
            ObjectError error = new ObjectError("globalError", exception.getMessage());
            bindingResult.addError(error);
        } catch (Exception exception) {
            ObjectError error = new ObjectError("globalError", "Internal error occurred");
            bindingResult.addError(error);
        }

        redirectAttributes.addFlashAttribute(
            "org.springframework.validation.BindingResult.authDto",
            bindingResult
        );
        return "redirect:/ui/registration";
    }


    @GetMapping("/login")
    public String loginPage(
        Model model,
        @RequestParam(value = "error", required = false)
        String oauthError
    ) {
        if (model.asMap().isEmpty()) {
            model.addAttribute("authDto", new LoginRequestDto());
            if (oauthError != null) {
                model.addAttribute("error", oauthError);
            }
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String authenticateCallback(
        @Valid @ModelAttribute("authDto")
        LoginRequestDto authDto,
        BindingResult bindingResult,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes
    ) {
        if (authDto == null || bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.authDto",
                bindingResult
            );
            return "redirect:" + EndpointConstants.LOGIN_PAGE_ENDPOINT;
        }
        try {
            response.addCookie(authenticationService.login(authDto));
            return "redirect:/ui/main";
        } catch (AuthenticationException exception) {
            ObjectError error = new ObjectError("globalError", exception.getMessage());
            bindingResult.addError(error);
        } catch (Exception exception) {
            ObjectError error = new ObjectError("globalError", "Internal error occurred");
            bindingResult.addError(error);
        }

        redirectAttributes.addFlashAttribute(
            "org.springframework.validation.BindingResult.authDto",
            bindingResult
        );
        return "redirect:" + EndpointConstants.LOGIN_PAGE_ENDPOINT;
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        response.addCookie(this.authenticationService.logout());
        return "redirect:" + EndpointConstants.LOGIN_PAGE_ENDPOINT;
    }

    @GetMapping("/main")
    public String mainPage() {
        return "index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-page")
    public String adminPage(Model model) {
        model.addAttribute("allReports", reportService.findAll());
        return "admin-page";
    }
}
