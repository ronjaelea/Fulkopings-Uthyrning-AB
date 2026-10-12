package se.gritacademy.fulkopingsuthyrning.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(nullable = false)
    private Date rentalDate;

    @Column
    private Date returned;

    public Rental() {
    }

    public Rental(Long id, Date rentalDate, Date returned) {
        this.id = id;
        this.rentalDate = rentalDate;
        this.returned = returned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getReturned() {
        return returned;
    }

    public void setReturned(Date returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", rentalDate=" + rentalDate +
                ", returned=" + returned +
                '}';
    }
}
