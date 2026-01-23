package root.database.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * DATABASE ENTITY za cijenu članarstba
 */
@Entity
@Table(name = "membership")
public class MembershipEntity {

    @Id
    private Integer id = 1; // single row settings

    @Column(nullable = false)
    private double monthly;

    @Column(nullable = false)
    private double yearly;

    public Integer getId() { return id; }
    public double getMonthly() { return monthly; }
    public double getYearly() { return yearly; }

    public void setMonthly(double monthly) { this.monthly = monthly; }
    public void setYearly(double yearly) { this.yearly = yearly; }
}
