package se.gritacademy.fulkopingsuthyrning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.gritacademy.fulkopingsuthyrning.model.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    // true om produkten redan är uthyrd (finns i rentals med returnedDate null)
    // används för att neka dubbeluthyrning samt update av uthyrd produkt
    boolean existsByProductIdAndReturnedDateIsNull(Long productId);

    /**
     * bägge metoderna nedan används för att kunna kasta ConflictException
     * för delete i respektive service. 409 hade skickats till klienten
     * ändå (DataIntegrityViolation finns i GlobalExceptionHandler),
     * men jag ville skicka ett tydligare meddelande
     */
    boolean existsByPersonId(Long personId);

    boolean existsByProductId(Long productId);

}
