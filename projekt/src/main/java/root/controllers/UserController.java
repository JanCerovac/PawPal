package root.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import root.database.entites.UserEntity;
import root.database.repositories.UserRepository;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Iterable<UserEntity> getUsers() {
        return this.userRepository.findAll();
    }


    @GetMapping("/owner/edit")
    public String editAccount(Authentication authentication, HttpServletRequest request) {
        return "edit_owner";
    }
}