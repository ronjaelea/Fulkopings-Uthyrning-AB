package se.gritacademy.fulkopingsuthyrning.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "name is required")
    @NotBlank(message = "name cannot be blank")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "category is required")
    @NotBlank(message = "category cannot be blank")
    private String category;

    @Column(nullable = false)
    @NotNull
    @Positive(message = "pricePerDay must be a positive integer")
    private Integer pricePerDay;

    public Product() {
    }

    public Product(Long id, String name, String category, Integer pricePerDay) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.pricePerDay = pricePerDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Integer price) {
        this.pricePerDay = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + pricePerDay +
                '}';
    }
}
