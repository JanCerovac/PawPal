package root.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import root.database.entites.DogEntity;
import root.database.entites.OwnerEntity;

import java.util.Optional;

/**
 * DATABASE REPOSITORY za upravljanje DogEntity-em
 */
public interface DogRepository extends JpaRepository<DogEntity, Long> {
    void deleteByUsernameAndName(String username, String name);

    void deleteById(Long id);
}
