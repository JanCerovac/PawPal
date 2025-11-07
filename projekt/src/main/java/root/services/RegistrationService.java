package root.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import root.database.entites.UserEntity;
import root.database.repositories.UserRepository;

/**
 * service za registraciju korisnika
 * (trenutno samo sprema korisnike u memoriju)
 */
@Service
public class RegistrationService {

    // naši korisnici u memoriji
    private final UserRepository userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    // Spring koristi 'Depandencu Injection' kako bi
    // stvorio ove varijable
    public RegistrationService(
            UserRepository userDetailsManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * registracija korisnika
     * @param username username/email
     * @param rawPassword šifra unesena u form
     * @param role može biti OWNER ili WALKER
     */
    public void register(String username, String rawPassword, String role) {
        // ne dopuštaj već registriranom korisniku
        // ponovnu registraciju
        if (userDetailsManager.existsByUsername(username)) {
            throw new RuntimeException("User already exists");
        }

        // napravi Spring Security korisnika
        UserEntity user = new UserEntity(username);
        user.setPassword(passwordEncoder.encode(rawPassword));

        // TODO: makni ovo
        user.setRole(role);

        // spremi korisnika u memoriju
        userDetailsManager.save(user);
    }
}