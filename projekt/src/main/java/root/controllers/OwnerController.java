package root.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import root.data_objects.Dog;
import root.data_objects.Reservation;
import root.data_objects.NotificationReserve;
import root.services.GoogleCalendarService;
import root.services.OwnerService;

/**
 * kontroler za vlasnika
 */
@Controller
public class OwnerController {

    private final OwnerService ownerService;

    private final GoogleCalendarService googleCalendarService;
    private final SimpMessagingTemplate messagingTemplate;

    public OwnerController(
            OwnerService ownerService,
            GoogleCalendarService googleCalendarService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.ownerService = ownerService;
        this.googleCalendarService = googleCalendarService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * GET endpoint za izmjenu podataka psa
     */
    @GetMapping("/edit/owner/edit")
    public String edit_dog(@ModelAttribute Dog dog, Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("dog", dog); // for form pre-fill
        return "edit_dog";
    }

    /**
     * GET endpoint za dodavanje psa u bazu
     */
    @GetMapping(value = "/edit/owner/add")
    public String add(Model model, Authentication auth) {
        return "add_dog";
    }

    /**
     * POST endpoint za spremanje podataka u bazu
     */
    @PostMapping("/edit/owner/edit")
    public String edit_dog(@ModelAttribute Dog dog, Authentication auth) {
        ownerService.removeDog(dog.id());
        ownerService.addDog(auth.getName(), dog);
        return "redirect:/";
    }

    /**
     * POST endpoint za dodavanje psa u bazu
     */
    @PostMapping(value = "/edit/owner/add")
    public String add(@ModelAttribute Dog dog, Authentication auth) {
        ownerService.addDog(auth.getName(), dog);
        return "redirect:/";
    }

    /**
     * POST endpoint za brisanje psa
     */
    @PostMapping(value = "/edit/owner/delete")
    public String remove_dog(@ModelAttribute Dog dog, Authentication auth) {
        ownerService.removeDog(auth.getName(), dog);
        return "redirect:/";
    }

    /**
     * POST endpoint za rezerviranje šetnji
     */
    @PostMapping(value = "reserve")
    public String reserve(@ModelAttribute Reservation reservation, Authentication auth) {
        try {
            googleCalendarService.markReserved(reservation.eventId(), auth.getName(), reservation.address(), reservation.notes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        NotificationReserve msg = new NotificationReserve(
                auth.getName(),
                reservation.eventId(),
                reservation.type(),
                reservation.date(),
                reservation.duration(),
                reservation.address(),
                reservation.notes()
        );

        // pošalji notification šetaču čija je šetnja rezervirana
        messagingTemplate.convertAndSendToUser(
                reservation.walkerUsername(),
                "/queue/notifications",
                msg
        );

        return "redirect:/";
    }
}
