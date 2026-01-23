package root.database.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import root.database.entites.OwnerEntity;

import java.util.Optional;

/**
 * DATABASE REPOSITORY za upravljanje OwnerEntity-em
 */
public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {
    Optional<OwnerEntity> findByUsername(String username);

    @Transactional
    void deleteByUsername(String username);
}
