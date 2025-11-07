package root.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import root.data_objects.SetupRequest;
import root.database.repositories.UserRepository;
import root.services.SetupService;

@Controller
public class RootController {

    private final SetupService setupService;

    private final UserRepository repository;

    // Spring Inject konstruktor
    public RootController(
            SetupService setupService,
            UserRepository repository
    ) {
        this.setupService = setupService;
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

    /**
     * Setup page:
     * - korisnik odabire WALKER / OWNER
     * - ako korisnik ima definiran role -> redirect '/
     */
    @GetMapping("/setup")
    public String setup(Model model, Authentication auth) {
        var user = repository.findByUsername(auth.getName())
                .orElseThrow();

        if (user.getRole().isPresent()) {
            return "redirect:/";
        }

        model.addAttribute("username", user.getUsername());

        return "setup";
    }

    /**
     * --- WORK IN PROGRESS ---
     * Odgovoran endpoint za 'setup'
     */
    @PostMapping("/setup")
    public String setupRole(
            @RequestBody SetupRequest req,
            Model model
    ) {
        if (req.role().equals("WALKER")) {
            setupService.setupWalker(req.username(), req.experienceYears(), req.availability());
        } else if (req.role().equals("OWNER")) {
            setupService.setupOwner(req.username(), req.petName(), req.address());
        } else {
            model.addAttribute("error", "Invalid role");
            return "setup";
        }

        // definiraj 'role'
        repository.setRoleForUsername(req.username(), req.role());
        return "redirect:/";
    }
}