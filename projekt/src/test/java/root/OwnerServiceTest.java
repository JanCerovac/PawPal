package root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import root.data_objects.Dog;
import root.database.entites.OwnerEntity;
import root.database.repositories.OwnerRepository;
import root.services.OwnerService;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    private OwnerEntity owner;

    @BeforeEach
    void setup(){
        owner = new OwnerEntity("testUser");
    }

    @Test
    @DisplayName("Dodavanje psa vlasniku")
    void addingDogToOwner() {
        when(ownerRepository.findByUsername("testUser")).thenReturn(Optional.of(owner));

        Dog dog = new Dog(
                1L,
                "Rex",
                "Labrador",
                "3",
                "High",
                "Yes",
                "Healthy",
                "Friendly"
        );

        ownerService.addDog("testUser", dog);

        assertEquals(1, owner.getDogs().size());
    }

    @Test
    @DisplayName("Dodavanje psa nepostojećem vlasniku")
    void addingDogToNonexistingOwner() {

        Dog dog = new Dog(
                1L,
                "Rex",
                "Labrador",
                "3",
                "High",
                "Yes",
                "Healthy",
                "Friendly"
        );

        assertThrows(NoSuchElementException.class, () -> {
            ownerService.addDog("nonexistent", dog);
        });
    }

    @Test
    @DisplayName("Dodavanje psa s minimalnim podacima")
    void dogWithMinimalData() {

        when(ownerRepository.findByUsername("testUser"))
                .thenReturn(Optional.of(owner));

        Dog dog = new Dog(
                0L,
                "A",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        assertDoesNotThrow(() -> ownerService.addDog("testUser", dog));
        assertEquals("A", owner.getDogs().get(0).getName());
    }
}
