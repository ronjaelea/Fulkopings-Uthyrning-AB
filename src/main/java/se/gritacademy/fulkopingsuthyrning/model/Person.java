package se.gritacademy.fulkopingsuthyrning.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(nullable = false)
    @NotBlank(message = "Firstname cannot be blank")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Lastname cannot be blank")
    private String lastName;

    @Column(unique = true, nullable = false)
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Make sure you've entered a correct email address"
    )
    private String email;

/*
    @Column(nullable = false)
    private Date created;
*/


    public Person() {
    }

    public Person(Long id, String firstName, String lastName, String email/*, Date created*/) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
//        this.created = new Date();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

/*    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }*/

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}