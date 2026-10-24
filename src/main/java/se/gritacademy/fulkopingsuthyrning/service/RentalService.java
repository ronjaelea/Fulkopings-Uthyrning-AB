package se.gritacademy.fulkopingsuthyrning.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import se.gritacademy.fulkopingsuthyrning.model.Person;
import se.gritacademy.fulkopingsuthyrning.model.Product;
import se.gritacademy.fulkopingsuthyrning.model.Rental;
import se.gritacademy.fulkopingsuthyrning.repository.PersonRepository;
import se.gritacademy.fulkopingsuthyrning.repository.ProductRepository;
import se.gritacademy.fulkopingsuthyrning.repository.RentalRepository;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class RentalService {

    private static final long MINUTES_PER_DAY = 24 * 60; // för att få antal påbörjade dagar för totalCost

    private final RentalRepository rentalRepository;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;

    public RentalService(RentalRepository rentalRepository, PersonRepository personRepository, ProductRepository productRepository) {
        this.rentalRepository = rentalRepository;
        this.personRepository = personRepository;
        this.productRepository = productRepository;
    }

    public List<Rental> listAll() {
        return rentalRepository.findAll();
    }

    public Rental findById(Long id) {
        if(!rentalExists(id))
            throw new EntityNotFoundException("Rental with id " + id + " was not found");
        return rentalRepository.findById(id).get();
    }

/*
    public List<Rental> listByUser(User user) {
        // find all rentals by one user (full history or only active)
        // return list

    }
*/

    public Rental newRental(Long personId, Long productId) {
        Person person = personRepository.findById(personId).get();
        Product product = productRepository.findById(productId).get();
        // if person doesn't exist, throw error (404)
        // else if product doesn't exist, throw error (404)
        // else if product isn't available, throw error (409)

        Rental newRental = new Rental();
        newRental.setRentalDate(Instant.now());
        newRental.setPerson(person);
        newRental.setProduct(product);

        return rentalRepository.save(newRental);
    }

    public void returnProduct(Long rentalId) {

        Product product = rentalRepository.findById(rentalId).get().getProduct();

    }

/*
    private Integer calculateTotalCost(Instant rentalDate, Instant returnedDate) {
        // dagar mellan datumen * pricePerDay
        return totalCost;
    }
*/

    // ska jag ha med delete(Long) och update(Rental) ?

    private boolean rentalExists(Long id) {
        return rentalRepository.findById(id).isPresent();
    }

}
