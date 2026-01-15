package root.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import root.data_objects.*;
import root.database.entites.NotificationEntity;
import root.database.repositories.NotificationRepository;
import root.database.repositories.OwnerRepository;
import root.database.repositories.WalkRepository;
import root.database.repositories.WalkerRepository;
import root.services.GoogleCalendarService;
import root.services.ProfileService;
import root.services.RealtimeNotifier;
import root.services.SetupService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * kontroler za upravljanje korisničkim profilom.
 */
@Controller
public class ProfileController {

    private final WebClient webClient;

    private final SetupService service;
    private final ProfileService profileService;

    private final OwnerRepository ownerRepository;
    private final WalkerRepository walkerRepository;
    private final NotificationRepository notificationRepository;

    private final GoogleCalendarService googleCalendarService;
    private final RealtimeNotifier realtimeNotifier;

    public ProfileController(
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


    @GetMapping(value = "/edit/owner")
    public String edit(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());

        return "edit_owner";
    }

    @PostMapping(value = "/edit/owner/add")
    public String add(@ModelAttribute Dog dog, Authentication auth) {
        profileService.addDog(auth.getName(), dog);
        return "redirect:/";
    }




    @PostMapping("/notifications/response")
    @ResponseBody
    public void respond(
//            @PathVariable String id,
            @RequestBody Map<String, String> body,
            Authentication auth
    ) {
        String action = body.get("action");

        // Validate ownership
//        NotificationEntity n = notificationRepository.findById(id).orElseThrow();
//        if (!n.getRecipientId().equals(auth.getName())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }

        if ("ACCEPTED".equals(action)) {
            realtimeNotifier.pushToUser(
                    "user2",
                    "ACCEPTED"
            );
        } else if ("DECLINED".equals(action)) {
            realtimeNotifier.pushToUser(
                    "user2",
                    "DECLINED"
            );
        }

//        n.setReadAt(Instant.now());
//        notificationRepository.save(n);
    }





    @PostMapping(value = "/edit/owner/delete")
    public String remove_dog(@ModelAttribute Dog dog, Authentication auth) {
        profileService.removeDog(auth.getName(), dog);
        return "redirect:/";
    }

    @GetMapping("/edit/owner/edit")
    public String edit_dog(@ModelAttribute Dog dog, Model model, Authentication auth) {
        System.out.println(dog);
        model.addAttribute("username", auth.getName());
        model.addAttribute("dog", dog); // for form pre-fill
        return "edit_owner_edit";
    }

    @PostMapping("/edit/owner/edit")
    public String edit_dog(@ModelAttribute Dog dog, Authentication auth) {
        profileService.removeDog(dog.id());
        profileService.addDog(auth.getName(), dog);
        return "redirect:/";
    }


    @PostMapping("/edit/walker/delete")
    public String remove_event(@ModelAttribute @RequestParam String id, Authentication auth) {

        return "redirect:/";
    }
}