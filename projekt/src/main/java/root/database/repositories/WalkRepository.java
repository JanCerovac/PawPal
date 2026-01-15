package root.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import root.database.entites.WalkEntity;
import root.database.entites.WalkerEntity;

import java.util.Optional;

/**
 * --- WORK IN PROGRESS --
 */
public interface WalkRepository extends JpaRepository<WalkEntity, Long> {
}
