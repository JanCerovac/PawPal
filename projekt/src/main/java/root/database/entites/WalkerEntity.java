package root.database.entites;

import jakarta.persistence.*;

/**
 * --- WORK IN PROGRESS ---
 * Entity profila WALKER
 */
@Entity
@Table(name = "profile_walkers")
public class WalkerEntity {
    @Id
    private String username;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "username",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_owner")
    )
    private UserEntity user;

    @Column(nullable = false)
    private Integer experienceYears;

    @Column
    private String availability;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.username = user.getUsername();
        this.user = user;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
