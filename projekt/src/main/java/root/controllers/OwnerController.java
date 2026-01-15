package root.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import root.data_objects.Dog;
import root.services.OwnerService;

@Controller
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(
            OwnerService ownerService
    ) {
        this.ownerService = ownerService;
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
}
