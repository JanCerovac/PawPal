package root.database.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.database.entites.UserEntity;

import java.util.Optional;

/**
 * Repositoriji za UserEntity, primarni način za interakciju za bazom podataka.
 * 'Extends' JpaRepository kako bi mogli koristiti CRUD funkcije (save, findAll, deleteById, etc.).
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    // pronađi korisnika 'by username'
    Optional<UserEntity> findByUsername(String username);

    // pronađi korisnika 'by email'
    Optional<UserEntity> findByEmail(String email);

    // provjere postojanosti
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // promjeni korisnikovu ulogu
    @Transactional
    @Modifying @Query("UPDATE UserEntity user SET user.role = :role WHERE user.username = :username")
    void setRoleForUsername(@Param("username") String username, @Param("role") String role);

    // izbriši korisnika
    @Transactional
    void deleteByUsername(String username);
}
