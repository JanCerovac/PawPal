package root;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import root.database.repositories.UserRepository;
import root.services.RegistrationService;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegistrationService registrationService;


    @Test
    @DisplayName("Obriši korisnika po korisničkom imenu")
    void deleteUserByUserName() {

        String username = "userToDelete";

        registrationService.delete(username);

        verify(userRepository, times(1)).deleteByUsername(username);
    }
}
