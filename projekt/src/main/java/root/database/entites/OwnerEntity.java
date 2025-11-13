package root.database.entites;

import jakarta.persistence.*;
import root.data_objects.Dog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * --- WORK IN PROGRESS ---
 * Entity profila OWNER
 */
@Entity
@Table(name = "owners")
public class OwnerEntity {
    @Id
    private String username;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<DogEntity> dogs = new ArrayList<>();

    private OwnerEntity() {}

    public OwnerEntity(String username) {
        this.username = username;
    }

    public void addDog(DogEntity dog) {
        dogs.add(dog);
    }

    public String getUsername() {
        return username;
    }

    public List<DogEntity> getDogs() {
        return dogs;
    }
}
