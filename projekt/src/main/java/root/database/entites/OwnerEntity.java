package root.database.entites;

import jakarta.persistence.*;

/**
 * --- WORK IN PROGRESS ---
 * Entity profila OWNER
 */
@Entity
@Table(name = "profile_owners")
public class OwnerEntity {
    @Id
    private String username;

    @Column(nullable = false)
    private String petName;

    @Column(nullable = false)
    private String address;

    public String getPetName() {
        return petName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
