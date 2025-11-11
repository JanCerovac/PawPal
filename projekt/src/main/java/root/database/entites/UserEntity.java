package root.database.entites;

import jakarta.persistence.*;

import java.util.Optional;

/**
 * Entity našeg korisnika
 */
@Entity
@Table(name = "users")
public class UserEntity {

    // TODO: zamjeni ovo sa 'username'
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    // govori nam je li korisnik 'aktivan'
    private Boolean enabled = true;

    // WORKER / OWNER
    @Column(name = "role")
    private String role;

    // User Security zahtjeva privatni konstrukor
    private UserEntity() {}

    public UserEntity(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Optional<String> getRole() {
        return Optional.ofNullable(role);
    }

    public void setRole(String role) {
        this.role = role;
    }
}
