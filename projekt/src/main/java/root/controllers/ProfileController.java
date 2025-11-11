package root.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import root.data_objects.Dog;
import root.data_objects.Walker;
import root.services.SetupService;

/**
 * kontroler za upravljanje korisničkim profilom.
 */
@Controller
public class ProfileController {

    private final SetupService service;

    public ProfileController(SetupService service) {
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
