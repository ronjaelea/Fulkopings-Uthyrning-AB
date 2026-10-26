package se.gritacademy.fulkopingsuthyrning.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import se.gritacademy.fulkopingsuthyrning.controller.dto.RentalRequest;
import se.gritacademy.fulkopingsuthyrning.controller.dto.RentalResponse;
import se.gritacademy.fulkopingsuthyrning.exception.ConflictException;
import se.gritacademy.fulkopingsuthyrning.exception.ResourceNotFoundException;
import se.gritacademy.fulkopingsuthyrning.model.Person;
import se.gritacademy.fulkopingsuthyrning.model.Product;
import se.gritacademy.fulkopingsuthyrning.model.Rental;
import se.gritacademy.fulkopingsuthyrning.repository.PersonRepository;
import se.gritacademy.fulkopingsuthyrning.repository.ProductRepository;
import se.gritacademy.fulkopingsuthyrning.repository.RentalRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class RentalService {

    private final RentalRepository rentalRepository;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;

    public RentalService(RentalRepository rentalRepository, PersonRepository personRepository, ProductRepository productRepository) {
        this.rentalRepository = rentalRepository;
        this.personRepository = personRepository;
        this.productRepository = productRepository;
    }

    public List<RentalResponse> listAll() {
        return rentalRepository.findAll().stream()
                .map(RentalResponse::from)
                .toList(); // gör om List<Rental> som returneras från findAll(), till List<RentalResponse>
    }

    public RentalResponse findById(Long id) {
        return RentalResponse.from(rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", id)));
    }

    public RentalResponse create(RentalRequest request) {
        Person person = personRepository.findById(request.personId())
                .orElseThrow(() -> new ResourceNotFoundException("Person", request.personId()));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.productId()));

        // en Product kan inte hyras ut om den redan finns i en aktiv Rental
        if (rentalRepository.existsByProductIdAndReturnedDateIsNull(product.getId()))
            throw new ConflictException("Product with id " + product.getId() + " is already rented out");

        Rental rental = new Rental();
        rental.setPerson(person);
        rental.setProduct(product);
        rental.setRentalDate(Instant.now());

        return RentalResponse.from(rentalRepository.save(rental));
    }

    public RentalResponse returnRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", id));

        if (rental.getReturnedDate() != null)
            throw new ConflictException("Rental with id " + id + " has already been returned");

        /*
         getReturnedDate kan ej användas som argument till kostnadsberäkningen
         eftersom objektet inte är uppdaterat i db förrän transaktionen är klar.
         samma Instant som går in i db måste vara det som används för att beräkna kostnad,
         därför initiera variabeln now först.
         */
        Instant now = Instant.now();
        rental.setReturnedDate(now);
        rental.setTotalCost(calculateCost(rental.getRentalDate(), now, rental.getProduct().getPricePerDay()));

        return RentalResponse.from(rental);
    }

    /**
     * kostnaden är per påbörjad 24 timmars-period.
     * Math.max för att få åtminstone 1 påbörjad dag, ceilDiv rundar upp.
     * 1440 = minuter per dag.
     * använder toMinutes() ist för toDays() för att de rundar neråt (1d 23h = 1d).
     * upp till en minut längre än 24h blir nu alltså 1 dag men det får vara acceptabelt
     */
    private int calculateCost(Instant rentalDate, Instant returnedDate, int pricePerDay) {
        long minutes = Duration.between(rentalDate, returnedDate).toMinutes();
        long days = Math.max(1, Math.ceilDiv(minutes, 1440)); // 1440 = minuter per dag

        return Math.toIntExact(days * pricePerDay);
    }
}
