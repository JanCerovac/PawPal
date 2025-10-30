package root.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import root.login.user.RegistrationRequest;
import root.login.user.RegistrationService;

/**
 * kontroler za registarciju, procesiranje registracije, login, itd.
 */
@Controller
public class AuthController {

    private final RegistrationService registrationService;

    // 'inject' RegistrationService
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * pokaži stranicu za registraciju
     * (GET /register)
     */
    @GetMapping("/register")
    public String showRegister(Model model) {
        // dodaj prazni RegistrationRequest
        // da se Thymeleaf može vezati za njega
        model.addAttribute("registrationRequest", new RegistrationRequest());

        // templates/register.html
        return "register";
    }

    /**
     * registriraj korisnika
     * (POST /register)
     */
    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute RegistrationRequest registrationRequest,
            BindingResult result,
            HttpServletRequest request
    ) {
        // ako je validacija neuspješna,
        // vrati se nazad za register.html
        // i pokaži greške
        if (result.hasErrors()) {
            return "register";
        }

        try {
            // stvori korisnika u memoriji
            registrationService.register(registrationRequest.getEmail(), registrationRequest.getPassword());
        } catch (RuntimeException e) {
            // ako korisnik postoji, dodaj error u html
            result.rejectValue("email", "error.registrationRequest", e.getMessage());
            return "register";
        }

        // ako je registracija uspješna,
        // pošalji korisnika na login
        return "redirect:/login";
    }

    /**
     * login stranica
     * (GET /login)
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
