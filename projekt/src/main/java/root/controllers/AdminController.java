package root.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import root.data_objects.User;
import root.database.entites.MembershipEntity;
import root.database.repositories.MembershipRepository;
import root.database.repositories.OwnerRepository;
import root.database.repositories.UserRepository;
import root.database.repositories.WalkerRepository;

import java.util.Objects;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final WalkerRepository walkerRepository;
    private final OwnerRepository ownerRepository;

    private final MembershipRepository membershipRepository;

    public AdminController(
            UserRepository userRepository,
            WalkerRepository walkerRepository,
            OwnerRepository ownerRepository,
            MembershipRepository membershipRepository
    ) {
        this.userRepository = userRepository;
        this.walkerRepository = walkerRepository;
        this.ownerRepository = ownerRepository;
        this.membershipRepository = membershipRepository;
    }

    // stranica za admina
    @GetMapping("/admin")
    public String admin(Model model) {
        var users = new java.util.ArrayList<>(userRepository.findAll()
                .stream().map(
                        u -> new User(u.getUsername(), u.getRole().orElseThrow(), u.getEmail().orElse(null))
                ).toList());

        users.removeIf(u -> Objects.equals(u.role(), "ADMIN"));
        model.addAttribute("users", users);

        var price = membershipRepository.findById(1).orElseGet(() -> {
            var p = new MembershipEntity();
            p.setMonthly(10.0);
            p.setYearly(100.0);
            return membershipRepository.save(p);
        });

        model.addAttribute("price", price);

        return "admin";
    }

    /**
     * POST endpoint za brisanje korisnika iz baze podataka
     */
    @PostMapping("/admin/delete")
    public String delete(@RequestParam String username, Model model) {
        var role = userRepository.findByUsername(username).orElseThrow().getRole();

        // izbriši prvo profil...
        if (role.orElseThrow().equals("OWNER")) {
            ownerRepository.deleteByUsername(username);
        } else {
            walkerRepository.deleteByUsername(username);
        }

        // ... pa onda korisnika
        userRepository.deleteByUsername(username);

        return "redirect:/admin";
    }

    /**
     * POST endpoint za mijenjanje cijene članarstva
     **/
    @PostMapping("/admin/price")
    public String updatePrice(@RequestParam double monthly, @RequestParam double yearly) {

        var price = membershipRepository.findById(1).orElseGet(MembershipEntity::new);
        price.setMonthly(monthly);
        price.setYearly(yearly);
        membershipRepository.save(price);

        return "redirect:/admin";
    }
}