package root.database.entites;

import jakarta.persistence.*;

/**
 * DATABASE ENTITY za šetnju
 */
@Entity
@Table(name = "walks")
public class WalkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private Integer price;
    private Integer duration;
    private String datetime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "walker_username", nullable = false) // foreign key column
    private WalkerEntity walker;

    private WalkEntity() {}

    public WalkEntity(String type, Integer price, Integer duration, String datetime, WalkerEntity walker) {
        this.type = type;
        this.price = price;
        this.duration = duration;
        this.datetime = datetime;
        this.walker = walker;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public WalkerEntity getWalker() {
        return walker;
    }
}
