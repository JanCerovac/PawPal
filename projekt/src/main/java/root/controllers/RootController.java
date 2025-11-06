package root.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String index(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            var role = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(a -> a.startsWith("ROLE_"))
                    .findFirst()
                    .orElse("ROLE_USER");
            model.addAttribute("username", auth.getName());
            model.addAttribute("role", role); // e.g. ROLE_OWNER
        }
        return "index";
    }
}
