package se.gritacademy.fulkopingsuthyrning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.gritacademy.fulkopingsuthyrning.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
