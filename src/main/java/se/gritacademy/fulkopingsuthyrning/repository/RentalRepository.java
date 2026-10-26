package se.gritacademy.fulkopingsuthyrning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.gritacademy.fulkopingsuthyrning.model.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    // true om produkten redan är uthyrd (finns i rentals med returnedDate null)
    // används för att neka dubbeluthyrning
    boolean existsByProductIdAndReturnedDateIsNull(Long productId);
}
