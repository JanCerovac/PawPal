package root.database.entites;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DATABASE ENTITY za profil WALKER
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

    @OneToMany(
            mappedBy = "walker",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<WalkEntity> walks = new ArrayList<>();

    public WalkerEntity() {}

    public WalkerEntity(String username, String name, String surname, String contact, String location) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.contact = contact;
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getContact() {
        return contact;
    }

    public String getLocation() {
        return location;
    }

    public void addWalk(WalkEntity walk) {
        walks.add(walk);
    }

    public List<WalkEntity> getWalks() {
        return walks;
    }
}
