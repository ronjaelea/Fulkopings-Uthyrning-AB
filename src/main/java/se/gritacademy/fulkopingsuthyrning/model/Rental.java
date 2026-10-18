package se.gritacademy.fulkopingsuthyrning.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(nullable = false)
    private Instant rentalDate;

    @Column
    private Instant returnedDate;

    @Column
    private Integer totalCost;

    public Rental() {
    }

    public Rental(Long id, Instant rentalDate, Instant returnedDate, Integer totalCost) {
        this.id = id;
        this.rentalDate = rentalDate;
        this.returnedDate = returnedDate;
        this.totalCost = totalCost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Instant rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Instant getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Instant returned) {
        this.returnedDate = returned;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", rentalDate=" + rentalDate +
                ", returnedDate=" + returnedDate +
                ", totalCost=" + totalCost +
                '}';
    }
}
