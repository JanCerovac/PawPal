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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring koristi 'Depandencu Injection' kako bi
    // stvorio ove varijable
    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
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
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("User already exists");
        }

        // napravi Spring Security korisnika
        UserEntity user = new UserEntity(username);
        user.setPassword(passwordEncoder.encode(rawPassword));

        // spremi korisnika u memoriju
        userRepository.save(user);
    }

    public void delete(String username) {
        userRepository.deleteUserForUsername(username);
    }
}