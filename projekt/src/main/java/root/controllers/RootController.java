package root.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import root.data_objects.Dog;
import root.data_objects.Filter;
import root.database.entites.WalkerEntity;
import root.database.repositories.OwnerRepository;
import root.database.repositories.UserRepository;
import root.database.repositories.WalkerRepository;
import root.services.GoogleCalendarService;

import java.util.List;

@Controller
public class RootController {

    private final UserRepository repository;
    private final OwnerRepository ownerRepository;
    private final WalkerRepository walkerRepository;

    private final GoogleCalendarService googleCalendarService;

    // Spring Inject konstruktor
    public RootController(
            UserRepository repository,
            OwnerRepository ownerRepository,
            WalkerRepository walkerRepository,
            GoogleCalendarService googleCalendarService
    ) {
        this.repository = repository;
        this.ownerRepository = ownerRepository;
        this.walkerRepository = walkerRepository;
        this.googleCalendarService = googleCalendarService;
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

        // ako korisnik nema ulogu, redirect na setup
        if (user.getRole().isEmpty()) {
            return "redirect:/setup";
        }

        model.addAttribute("role", user.getRole().get());

        // ako je korisnik ADMIN, redirect
        // default admin login:
        // username: admin
        // password: 1234
        if (user.getRole().get().equals("ADMIN")) {
            return "redirect:/admin";
        }

        if (user.getRole().get().equals("WALKER")) {
            var walker = walkerRepository.findByUsername(user.getUsername()).orElseThrow();
            model.addAttribute("walker", walker);

            try {
                var walks = googleCalendarService.readAllWalks().stream()
                        .filter(e -> e.getDescription().contains(walker.getName() + " " + walker.getSurname()))
                        .toList();

                // dodaj walker-ove šetnje u html
                model.addAttribute("walks", walks);
            } catch (Exception ignored) {}
        } else if (user.getRole().get().equals("OWNER")) {
            var dogs = ownerRepository.findByUsername(user.getUsername()).get().getDogs();

            // dodaj pse u html
            model.addAttribute("dogs", dogs.stream().map(
                    d -> new Dog(d.getId(), d.getName(), d.getBreed(), d.getAge(), d.getEnergylvl(), d.getTreat(), d.getHealth(), d.getSocial())
            ).toList());

            // dodaj sve šetače u html
            model.addAttribute("walkers", walkerRepository.findAll());

            try {
                var events = googleCalendarService.readAllWalks();

                // dodaj sve šetnje u html
                model.addAttribute("events", events);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return "homepage";
    }

    @PostMapping("/search")
    @ResponseBody
    public List<WalkerEntity> search(@RequestBody Filter filter) {
        return walkerRepository.findByLocation(filter.walkerLocation());
    }
}