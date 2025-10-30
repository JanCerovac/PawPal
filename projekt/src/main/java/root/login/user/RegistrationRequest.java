package root.login.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Object za registraciju
 */
public class RegistrationRequest {

    @NotBlank   // ne može biti prazno
    @Email      // mora biti email
    private String email;

    @NotBlank
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    // getters & setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
