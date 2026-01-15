package root.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import root.data_objects.Dog;
import root.data_objects.Walk;
import root.database.entites.DogEntity;
import root.database.entites.OwnerEntity;
import root.database.entites.WalkEntity;
import root.database.entites.WalkerEntity;
import root.database.repositories.DogRepository;
import root.database.repositories.OwnerRepository;
import root.database.repositories.UserRepository;
import root.database.repositories.WalkerRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    private final OwnerRepository ownerRepository;
    private final WalkerRepository walkerRepository;
    private final DogRepository dogRepository;

    public ProfileService(
            UserRepository userRepository,
            OwnerRepository ownerRepository,
            WalkerRepository walkerRepository,
            DogRepository dogRepository
    ) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.walkerRepository = walkerRepository;
        this.dogRepository = dogRepository;
    }

    public void addDog(String username, Dog dog) {
        Optional<OwnerEntity> ownerEntity = ownerRepository.findByUsername(username);

        ownerEntity.orElseThrow().addDog(new DogEntity(
                username,
                dog.name(),
                dog.breed(),
                dog.age(),
                dog.energyLevel(),
                dog.allowedTreats(),
                dog.healthcare(),
                dog.personality(),
                ownerEntity.orElseThrow()
        ));

        ownerRepository.save(ownerEntity.get());
    }

    @Transactional
    public void removeDog(String username, Dog dog) {
        String normalized = dog.name() == null ? "" : dog.name().trim();
        dogRepository.deleteByUsernameAndName(username, normalized);
    }

    @Transactional
    public void removeDog(Long dogId) {
        dogRepository.deleteById(dogId);
    }

    public void addWalk(String username, Walk walk) {
        Optional<WalkerEntity> walkerEntity = walkerRepository.findByUsername(username);

        walkerEntity.orElseThrow().addWalk(new WalkEntity(
                walk.type(), walk.price(), walk.duration(), walk.datetime(), walkerEntity.orElseThrow()
        ));

        walkerRepository.save(walkerEntity.get());
    }
}
