package root.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import root.data_objects.NotificationConfirm;
import root.data_objects.NotificationReserve;
import root.data_objects.Walk;
import root.database.repositories.WalkerRepository;
import root.services.GoogleCalendarService;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * kontroler za šetače
 */
@Controller
public class WalkerController {

    private final WalkerRepository walkerRepository;

    private final GoogleCalendarService googleCalendarService;
    private final SimpMessagingTemplate messagingTemplate;

    public WalkerController(
            WalkerRepository walkerRepository,
            GoogleCalendarService googleCalendarService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.walkerRepository = walkerRepository;
        this.googleCalendarService = googleCalendarService;
        this.messagingTemplate = messagingTemplate;
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
                    walker.getLocation(),
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

    /**
     * POST endpoint za prihvaćanje rezervacije
     */
    @PostMapping(value = "reservation/accept")
    public String accept_reservation(@ModelAttribute NotificationReserve notification, Authentication auth) {
        var walker = walkerRepository.findByUsername(auth.getName()).orElseThrow();
        var name = walker.getName() + " " + walker.getSurname();

        NotificationConfirm msg = new NotificationConfirm(
                name,
                notification.type(),
                notification.date(),
                notification.duration(),
                notification.address()
        );

        // pošalji notification vlasniku da je rezervacija prihvaćena
        messagingTemplate.convertAndSendToUser(
                notification.ownerUsername(),
                "/queue/notifications",
                msg
        );

        return "redirect:/";
    }
}
