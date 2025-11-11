package root.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import root.database.entites.OwnerEntity;

import java.util.Optional;

/**
 * --- WORK IN PROGRESS --
 */
public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {
    Optional<OwnerEntity> findByUsername(String username);
}
