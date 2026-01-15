package root.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import root.data_objects.*;
import root.database.repositories.NotificationRepository;
import root.database.repositories.OwnerRepository;
import root.database.repositories.WalkerRepository;
import root.services.GoogleCalendarService;
import root.services.ProfileService;
import root.services.RealtimeNotifier;
import root.services.SetupService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * kontroler za upravljanje korisničkim profilom.
 */
@Controller
public class WalkController {

    private final WebClient webClient;

    private final SetupService service;
    private final ProfileService profileService;

    private final OwnerRepository ownerRepository;
    private final WalkerRepository walkerRepository;
    private final NotificationRepository notificationRepository;

    private final GoogleCalendarService googleCalendarService;
    private final RealtimeNotifier realtimeNotifier;

    public WalkController(
            WebClient webClient,
            SetupService service,
            ProfileService profileService,
            OwnerRepository ownerRepository,
            WalkerRepository walkerRepository,
            NotificationRepository notificationRepository,
            GoogleCalendarService googleCalendarService,
            RealtimeNotifier realtimeNotifier
    ) {
        this.webClient = webClient;
        this.service = service;
        this.profileService = profileService;
        this.ownerRepository = ownerRepository;
        this.walkerRepository = walkerRepository;
        this.notificationRepository = notificationRepository;
        this.googleCalendarService = googleCalendarService;
        this.realtimeNotifier = realtimeNotifier;
    }

    @GetMapping(value = "walk/walker")
    public String walk_walker(
            Authentication auth,
            Model model
    ) {
        try {
            var walker = walkerRepository.findByUsername(auth.getName()).orElseThrow();

            var walks = googleCalendarService.readEvents().stream()
                    .filter(e -> e.getDescription().contains(walker.getName() + " " + walker.getSurname()))
                    .toList();

            model.addAttribute("walks", walks);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "walk_walker";
    }

    @GetMapping(value = "walk/owner")
    public String walk_owner(Model model) {
        try {
            var walks = googleCalendarService.readEvents();
            model.addAttribute("walks", walks);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "walk_owner";
    }



    @PostMapping("walk/owner/reserve")
    public String reserve(@ModelAttribute WalkReservation reservation, Authentication auth) {
        try {
            String ownerUsername = auth.getName();

            googleCalendarService.markReserved(
                    reservation.eventId(),
                    ownerUsername,
                    reservation.address(),
                    reservation.notes()
            );

            var walker = googleCalendarService.getPrivateProperty(reservation.eventId(), "username");

            realtimeNotifier.pushToUser(
                    walker,
                    new Notification(reservation.eventId(), "NEW RESERVATION", "bla bla", false)
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/walk/owner";
    }
}