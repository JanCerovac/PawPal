package root.services;

import org.springframework.stereotype.Service;
import root.data_objects.Dog;
import root.data_objects.Walker;
import root.database.entites.DogEntity;
import root.database.entites.OwnerEntity;
import root.database.entites.UserEntity;
import root.database.entites.WalkerEntity;
import root.database.repositories.OwnerRepository;
import root.database.repositories.UserRepository;
import root.database.repositories.WalkerRepository;

/**
 * Service koji prilaže stvara uloge.
 */
@Service
public class SetupService {

    private final UserRepository userRepository;
    private final WalkerRepository walkerRepository;
    private final OwnerRepository ownerRepository;

    // Spring Inject konstruktor
    public SetupService(
            UserRepository userRepository,
            WalkerRepository walkerRepository,
            OwnerRepository ownerRepository
    ) {
        this.userRepository = userRepository;
        this.walkerRepository = walkerRepository;
        this.ownerRepository = ownerRepository;
    }

    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    /**
     * Stvori WalkerEntity i priloži ga ulogiranom korisniku
     */
    public void setupWalker(
            String username,
            Walker walker
    ) {
        userRepository.setRoleForUsername(username, "WALKER");
        walkerRepository.save(new WalkerEntity(
                username,
                walker.name(),
                walker.surname(),
                walker.contact(),
                walker.location()
        ));
    }

    /**
     * Stvori OwnerEntity i priloži ga ulogiranom korisniku
     */
    public void setupOwner(
            String username,
            Dog dog
    ) {
        userRepository.setRoleForUsername(username, "OWNER");
        OwnerEntity owner = new OwnerEntity(username);
        owner.addDog(new DogEntity(
                username,
                dog.name(),
                dog.breed(),
                dog.age(),
                dog.energyLevel(),
                dog.allowedTreats(),
                dog.healthcare(),
                dog.personality(),
                owner
        ));

        ownerRepository.save(owner);
    }
}
