package root.controllers;

import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import root.data_objects.Dog;
import root.data_objects.Filter;
import root.data_objects.Walk;
import root.database.entites.WalkerEntity;
import root.database.repositories.OwnerRepository;
import root.database.repositories.UserRepository;
import root.database.repositories.WalkerRepository;
import root.services.GoogleCalendarService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

        if (user.getRole().isEmpty()) {
            return "redirect:/setup";
        }

        System.out.println(auth.getName());

        model.addAttribute("role", user.getRole().get());

        if (user.getRole().get().equals("WALKER")) {
            var walker = walkerRepository.findByUsername(user.getUsername()).orElseThrow();
            model.addAttribute("walker", walker);

            try {
                var walks = googleCalendarService.readEvents().stream()
                        .filter(e -> e.getDescription().contains(walker.getName() + " " + walker.getSurname()))
                        .toList();

                model.addAttribute("walks", walks);
            } catch (Exception ignored) {}
        } else if (user.getRole().get().equals("OWNER")) {
            var dogs = ownerRepository.findByUsername(user.getUsername()).get().getDogs();

            // dodaj pse u html
            model.addAttribute("dogs", dogs.stream().map(
                    d -> new Dog(d.getId(), d.getName(), d.getBreed(), d.getAge(), d.getEnergylvl(), d.getTreat(), d.getHealth(), d.getSocial())
            ).toList());

            model.addAttribute("walkers", walkerRepository.findAll());
        }

        return "homepage";
    }


    @PostMapping("/search")
    @ResponseBody
    public List<WalkerEntity> search(@RequestBody Filter filter) {
        return walkerRepository.findByLocation(filter.walkerLocation());
    }
}