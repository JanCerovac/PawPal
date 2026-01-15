package root.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import root.data_objects.RegistrationRequest;
import root.services.RegistrationService;

/**
 * kontroler za registarciju, procesiranje registracije, login, itd.
 */
@Controller
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;

    // 'inject' RegistrationService
    public AuthController(
            RegistrationService registrationService,
            AuthenticationManager authenticationManager
    ) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * pokaži stranicu za registraciju
     * (GET /register)
     */
    @GetMapping("/register")
    public String register(Model model) {
        // dodaj prazni RegistrationRequest
        // da se Thymeleaf može vezati za njega
        model.addAttribute("registrationRequest", new RegistrationRequest());

        // templates/registration.html
        return "registration";
    }

    /**
     * registriraj korisnika
     * (POST /register)
     */
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegistrationRequest registrationRequest,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // ako je validacija neuspješna,
        // vrati se nazad za registration.html
        // i pokaži greške
        if (result.hasErrors()) {
            return "registration";
        }

        try {
            // stvori korisnika u memoriji
            registrationService.register(
                    registrationRequest.getUsername(),
                    registrationRequest.getPassword()
            );
        } catch (RuntimeException e) {
            // ako korisnik postoji, dodaj error u html
            result.rejectValue("username", "error.registrationRequest", e.getMessage());
            return "registration";
        }

        // ulogiraj korisnika
        var authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                registrationRequest.getUsername(),
                registrationRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authRequest);

        // spremi u Security Context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // persist
        new HttpSessionSecurityContextRepository().saveContext(context, request, response);

        return "redirect:/";
    }

    /**
     * login stranica
     * (GET /login)
     */
    @GetMapping("/login")
    public String getLogin(Authentication authentication) {
        if (authentication != null)
            return "redirect:/";

        return "login";
    }

    @PostMapping("/delete")
    public String deleteAccount(Authentication authentication, HttpServletRequest request) {
        if (authentication == null)
            return "login";

        // logout
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        // delete
        try {
            registrationService.delete(authentication.getName());
        } catch (RuntimeException e) {
            System.err.println("Exception when trying to delete account " + authentication.getName());
        }

        return "redirect:/login?delete";
    }
}
