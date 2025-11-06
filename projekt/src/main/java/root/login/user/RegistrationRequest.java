package root.login.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Object za registraciju
 */
public class RegistrationRequest {

    @NotBlank   // ne može biti prazno
    private String username;

    @NotBlank
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    @NotBlank
    private String role = "OWNER";    // može biti OWNER ili WALKER

    // getters & setters
    public String getUsername() { return username; }
    public void setUsername(String email) { this.username = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
