package root.database.entites;

import jakarta.persistence.*;

/**
 * --- WORK IN PROGRESS ---
 * Entity profila WALKER
 */
@Entity
@Table(name = "walkers")
public class WalkerEntity {
    @Id
    private String username;

    private String name;
    private String surname;
    private String contact;
    private String location;

    private WalkerEntity() {}

    public WalkerEntity(String username, String name, String surname, String contact, String location) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.contact = contact;
        this.location = location;
    }
}
