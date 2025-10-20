package root.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
