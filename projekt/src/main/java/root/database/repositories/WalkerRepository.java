package root.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import root.database.entites.WalkerEntity;

import java.util.List;
import java.util.Optional;

/**
 * --- WORK IN PROGRESS --
 */
public interface WalkerRepository extends JpaRepository<WalkerEntity, Long> {
    Optional<WalkerEntity> findByUsername(String username);

    List<WalkerEntity> findByLocation(String location);
}
