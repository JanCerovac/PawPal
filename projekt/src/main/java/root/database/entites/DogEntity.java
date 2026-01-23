package root.database.entites;

import jakarta.persistence.*;

/**
 * DATABASE ENTITY za psa
 */
@Entity
@Table(name = "dogs")
public class DogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String name;
    private String breed;
    private String age;
    private String energylvl;
    private String treat;
    private String health;
    private String social;

    /**
     * vlasnik ovog entity-a je owner
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_username", nullable = false) // foreign key column
    private OwnerEntity owner;

    private DogEntity() {}

    public DogEntity(String username, String name, String breed, String age, String energylvl, String treat, String health, String social, OwnerEntity owner) {
        this.username = username;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.energylvl = energylvl;
        this.treat = treat;
        this.health = health;
        this.social = social;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getAge() {
        return age;
    }

    public String getEnergylvl() {
        return energylvl;
    }

    public String getTreat() {
        return treat;
    }

    public String getHealth() {
        return health;
    }

    public String getSocial() {
        return social;
    }

    public OwnerEntity getOwner() {
        return owner;
    }
}