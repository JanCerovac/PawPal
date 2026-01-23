package root.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import root.database.entites.DogEntity;
import root.database.entites.MembershipEntity;

/**
 * DATABASE REPOSITORY za upravljanje MembershipEntity-em
 */
public interface MembershipRepository extends JpaRepository<MembershipEntity, Integer> { }
