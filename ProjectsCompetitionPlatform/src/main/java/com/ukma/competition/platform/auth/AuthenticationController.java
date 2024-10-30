package com.ukma.competition.platform.auth;

import com.ukma.competition.platform.auth.dto.LoginRequestDto;
import com.ukma.competition.platform.auth.dto.RegistrationRequestDto;
import com.ukma.competition.platform.shared.exception.AuthenticationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

import java.util.List;

@Controller
@RequestMapping("/ui")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationController {

    AuthenticationService authenticationService;

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        if (model.asMap().isEmpty()) {
            model.addAttribute("authDto", new LoginRequestDto());
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
            List<Cookie> tokenCookies = authenticationService.register(authDto);
            tokenCookies.forEach(response::addCookie);
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


    @GetMapping("/login")
    public String loginPage(
        Model model,
        HttpServletResponse response,
        @RequestParam(value = "oauth_error", required = false)
        String oauthError
    ) {
        if (model.asMap().isEmpty()) {
            model.addAttribute("authDto", new LoginRequestDto());
            if (oauthError != null) {
                model.addAttribute("oauth_error", oauthError);
                Cookie oauthErrorCookieReset = new Cookie("oauth_error", "");
                oauthErrorCookieReset.setMaxAge(0);
                response.addCookie(oauthErrorCookieReset);
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
            List<Cookie> tokenCookies = authenticationService.login(authDto);
            tokenCookies.forEach(response::addCookie);
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

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin_page";
    }
}
