package root.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

@Controller
public class WalkerController {

    private final WalkerRepository walkerRepository;

    private final GoogleCalendarService googleCalendarService;

    public WalkerController(
            WalkerRepository walkerRepository,
            GoogleCalendarService googleCalendarService
    ) {
        this.walkerRepository = walkerRepository;
        this.googleCalendarService = googleCalendarService;
    }

    /**
     * Dodaj walk even u Google Calendar
     */
    @PostMapping(value = "walk/walker/submit")
    public String submit_walk(@ModelAttribute Walk walk, Authentication auth) {
        try {
            var walker = walkerRepository.findByUsername(auth.getName()).orElseThrow();
            var name = walker.getName() + " " + walker.getSurname();

            googleCalendarService.createWalkEvent(
                    name,
                    auth.getName(),
                    walk.type(),
                    walk.price(),
                    walk.duration(),
                    LocalDateTime.parse(walk.datetime())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }

    /**
     * Izbriši walk event from Google Calendar
     */
    @PostMapping(value = "walk/walker/delete")
    public String delete_walk(@RequestParam String id, Authentication auth) {
        try {
            googleCalendarService.deleteEvent(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }
}
