package root.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import root.data_objects.*;
import root.services.SetupService;

/**
 * kontroler za upravljanje korisničkim profilom.
 */
@Controller
public class SetupController {

    private final WebClient webClient;

    private final SetupService service;

    public SetupController(
            WebClient webClient,
            SetupService service
    ) {
        this.webClient = webClient;
        this.service = service;
    }

    /**
     * Setup page:
     * - korisnik odabire WALKER / OWNER
     * - ako korisnik ima definiran role -> redirect '/
     */
    @GetMapping("/setup")
    public String setup(Model model, Authentication auth) {
        if (service.getUser(auth.getName()).getRole().isPresent()) {
            return "redirect:/";
        }

        model.addAttribute("username", auth.getName());

        return "setup";
    }

    /**
     * GET endpoint za uređivanje podataka
     */
    @GetMapping("/owner/edit")
    public String editAccount(Authentication authentication, HttpServletRequest request) {
        return "add_dog";
    }

    /**
     * postavi role WALKER
     */
    @PostMapping(value="/setup", params = "form=walker")
    public String setupWalker(@ModelAttribute Walker walker, Authentication auth) {
        service.setupWalker(auth.getName(), walker);
        return "redirect:/";
    }

    /**
     * postavi role OWNER
     */
    @PostMapping(value="/setup", params = "form=owner")
    public String setupOwner(@ModelAttribute Dog dog, Authentication auth) {
        service.setupOwner(auth.getName(), dog);
        return "redirect:/";
    }
}