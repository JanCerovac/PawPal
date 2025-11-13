package root.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import root.database.repositories.OwnerRepository;
import root.database.repositories.UserRepository;
import root.database.repositories.WalkerRepository;

@Controller
public class RootController {

    private final UserRepository repository;
    private final OwnerRepository ownerRepository;
    private final WalkerRepository walkerRepository;

    // Spring Inject konstruktor
    public RootController(
            UserRepository repository,
            OwnerRepository ownerRepository,
            WalkerRepository walkerRepository
    ) {
        this.repository = repository;
        this.ownerRepository = ownerRepository;
        this.walkerRepository = walkerRepository;
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

        model.addAttribute("role", user.getRole().get());

        if (user.getRole().get().equals("WALKER")) {
            var walker = walkerRepository.findByUsername(user.getUsername());
            model.addAttribute("walker", walker.get());
        } else if (user.getRole().get().equals("OWNER")) {
            var dog = ownerRepository.findByUsername(user.getUsername()).get().getDogs().get(0);
            model.addAttribute("dog", dog);
        }

        return "homepage";
    }
}