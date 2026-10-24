package se.gritacademy.fulkopingsuthyrning.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant rentalDate;

    @Column
    private Instant returnedDate;

    @Column
    private Integer totalCost;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", rentalDate=" + rentalDate +
                ", returnedDate=" + returnedDate +
                ", totalCost=" + totalCost +
                ", person=" + person +
                ", product=" + product +
                '}';
    }
}
