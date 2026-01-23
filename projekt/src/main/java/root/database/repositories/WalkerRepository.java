package root.database.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import root.database.entites.WalkerEntity;

import java.util.List;
import java.util.Optional;

/**
 * DATABASE REPOSITORY za upravljanje WalkerEntity-em
 */
public interface WalkerRepository extends JpaRepository<WalkerEntity, Long> {
    Optional<WalkerEntity> findByUsername(String username);
    List<WalkerEntity> findByLocation(String location);

    @Transactional
    void deleteByUsername(String username);
}
