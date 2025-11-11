package root.services.login;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import root.database.entites.UserEntity;
import root.database.repositories.UserRepository;

/**
 * Prilagođeni UserDetailsService.
 * Učitava korisnika iz baze podataka i stvara Spring Security UserDetails objekt.
 *
 * Također, blokira login ako se korisnik pokušava prijaviti s email-om
 * čiji je 'provider' google, tako da se korisnik mora prijaviti preko OAuth.
 */
@Service
public class PawPalUserDetailsService implements UserDetailsService {

    // repositoriji za bazu podataka korisnika
    private final UserRepository repository;

    // Spring Inject konstruktor
    public PawPalUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Ovu funkciju poziva Spring Security nakon što korisnik pritisne 'Login'
     * @param username ono što korisnik napiše u 'login box'
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user;

        // 1) pokušaj pronaći korisnika preko 'username'
        if (repository.findByUsername(username).isPresent()) {
            user = repository.findByUsername(username).get();

            // 2) ako ne postoji preko 'username', ali postoji preko 'email' -> blokiraj login
        } else if (repository.findByEmail(username).isPresent()) {
            throw new DisabledException("This account uses Google sign-in. Please continue with Google.");

            // 3) ne postoji taj korisnik
        } else {
            throw new UsernameNotFoundException("User not found");
        }

        // vraćamo Spring Security User objekt
        return User.withUsername(user.getUsername())
                .password(user.getPassword().orElseThrow())
                .disabled(!user.getEnabled())
                .build();
    }
}
