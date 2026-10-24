package se.gritacademy.fulkopingsuthyrning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.gritacademy.fulkopingsuthyrning.model.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
