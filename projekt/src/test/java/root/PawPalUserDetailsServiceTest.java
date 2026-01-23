package root;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import root.database.entites.UserEntity;
import root.database.repositories.UserRepository;
import root.services.login.PawPalUserDetailsService;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PawPalUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PawPalUserDetailsService userDetailsService;

    @Test
    @DisplayName("Prijava korisnika u sustav")
    void userLogin() {
        UserEntity testUser = new UserEntity("testUser");
        testUser.setPassword("Password123");
        testUser.setEnabled(true);

        when(userRepository.findByUsername("testUser"))
                .thenReturn(Optional.of(testUser));

        UserDetails result = userDetailsService.loadUserByUsername("testUser");

        assertTrue(result.isEnabled());
        assertEquals("testUser", result.getUsername());
        assertEquals("Password123", result.getPassword());

        verify(userRepository, times(2)).findByUsername("testUser");
    }
}