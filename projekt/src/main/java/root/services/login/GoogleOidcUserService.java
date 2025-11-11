package root.services.login;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import root.database.entites.UserEntity;
import root.database.repositories.UserRepository;

/**
 * Prilagođeni OIDC user service koji intercept-a OAuth2 login
 * kako bi smo mogli stvoriti novog korisnika u našoj bazi podataka.
 */
@Service
public class GoogleOidcUserService extends OidcUserService {

    // repositoriji za bazu podataka korisnika
    private final UserRepository repository;

    // Spring Inject konstruktor
    public GoogleOidcUserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Ovu funkciju poziva Spring Security nakon što se korisnik prijavi preko Google-a.
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // koristimo 'default' OIDC korisnika
        OidcUser oidc = super.loadUser(userRequest);

        // ako korisnik ne postoji u bazi, spremi ga
        if (!repository.existsByEmail(oidc.getEmail())) {
            var user = new UserEntity(oidc.getName());

            user.setEmail(oidc.getEmail());

            // budući da je 'password' obavezan za Spring Security,
            // spremamo ga kao 'Access Token Hash'
            // (korisnik se neće moći prijaviti pomoću ovoga)
            user.setPassword(oidc.getAccessTokenHash());

            // spremi korisnika u bazu
            repository.save(user);
        }

        // vraćamo autoriziranog korisnika Spring Security-u
        return oidc;
    }
}
