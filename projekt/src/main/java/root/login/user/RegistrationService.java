package root.login.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * service za registraciju korisnika
 * (trenutno samo sprema korisnike u memoriju)
 */
@Service
public class RegistrationService {

    // naši korisnici u memoriji
    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    // Spring koristi 'Depandencu Injection' kako bi
    // stvorio ove varijable
    public RegistrationService(
            InMemoryUserDetailsManager userDetailsManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * registracija korisnika
     * @param username username/email
     * @param rawPassword šifra unesena u form
     */
    public void register(String username, String rawPassword) {
        // ne dopuštaj već registriranom korisniku
        // ponovnu registraciju
        if (userDetailsManager.userExists(username)) {
            throw new RuntimeException("User already exists");
        }

        // napravi Spring Security korisnika
        UserDetails user = User
                .withUsername(username)
                // enkodiraj šifru prije spremanja
                .password(passwordEncoder.encode(rawPassword))
                // ovdje odlučujemo o 'ulozi' korisnika
                .roles("USER")
                .build();

        // spremi korisnika u memoriju
        userDetailsManager.createUser(user);
    }
}
