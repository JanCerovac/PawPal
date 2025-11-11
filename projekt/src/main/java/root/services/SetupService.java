package root.services;

import org.springframework.stereotype.Service;
import root.database.entites.OwnerEntity;
import root.database.entites.WalkerEntity;
import root.database.repositories.OwnerRepository;
import root.database.repositories.UserRepository;
import root.database.repositories.WalkerRepository;

/**
 * Service koji prilaže 'role' (WALKER/OWNER) postojećim korisnicima.
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

    /**
     * --- WORK IN PROGRESS ---
     * Stvori WalkerEntity i priloži ga ulogiranom korisniku
     */
    public void setupWalker(
            String username,
            Integer experienceYears,
            String availability
    ) {
        var user = userRepository.findByUsername(username)
                .orElseThrow();

        var walker = new WalkerEntity();
        walker.setUser(user);
        walker.setExperienceYears(experienceYears);
        walker.setAvailability(availability);

        walkerRepository.save(walker);
    }

    /**
     * --- WORK IN PROGRESS ---
     * Stvori OwnerEntity i priloži ga ulogiranom korisniku
     */
    public void setupOwner(
            String username,
            String petName,
            String address
    ) {
        var user = userRepository.findByUsername(username)
                .orElseThrow();

        var owner = new OwnerEntity();
        owner.setUsername(user.getUsername());
        owner.setPetName(petName);
        owner.setAddress(address);

        ownerRepository.save(owner);
    }
}
