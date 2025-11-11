package root.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import root.database.repositories.UserRepository;

@Controller
public class RootController {

    private final UserRepository repository;

    // Spring Inject konstruktor
    public RootController(
            UserRepository repository
    ) {
        this.repository = repository;
    }

    /**
     * Home page.
     * - pretpostavlja da je korisnik ulogiran
     * - ako korisnik nema definiran role -> redirect '/setup'
     */
    @GetMapping("/")
    public String index(Model model, Authentication auth) {
        var user = repository.findByUsername(auth.getName())
                .orElseThrow();

        if (user.getRole().isEmpty()) {
            return "redirect:/setup";
        }

        return "index";
    }
}