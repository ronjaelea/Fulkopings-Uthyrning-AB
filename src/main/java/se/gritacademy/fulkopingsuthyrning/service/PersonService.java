package se.gritacademy.fulkopingsuthyrning.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.gritacademy.fulkopingsuthyrning.controller.dto.PersonRequest;
import se.gritacademy.fulkopingsuthyrning.exception.ConflictException;
import se.gritacademy.fulkopingsuthyrning.exception.ResourceNotFoundException;
import se.gritacademy.fulkopingsuthyrning.model.Person;
import se.gritacademy.fulkopingsuthyrning.repository.PersonRepository;
import se.gritacademy.fulkopingsuthyrning.repository.RentalRepository;

import java.util.List;

@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;
    private final RentalRepository rentalRepository;

    public PersonService(PersonRepository personRepository,  RentalRepository rentalRepository) {
        this.personRepository = personRepository;
        this.rentalRepository = rentalRepository;
    }

    public List<Person> listAll() {
        return personRepository.findAll();
    }

    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    }

    public Person create(PersonRequest request) {
        if (personRepository.existsByEmail(request.email()))
            throw new ConflictException("Email already exists");

        Person person = new Person();
        person.setFirstName(request.firstName());
        person.setLastName(request.lastName());
        person.setEmail(request.email());

        return personRepository.save(person);
    }

    public Person update(Long id, PersonRequest request) {
        Person person = findById(id); // checkar 404 före 409

        if (personRepository.existsByEmailAndIdNot(request.email(), person.getId()))
            throw new ConflictException("Email " + request.email() + " is already registered");

        person.setFirstName(request.firstName());
        person.setLastName(request.lastName());
        person.setEmail(request.email());

        return person;
    }

    public void delete(Long id) {
        if(!personRepository.existsById(id))
            throw new ResourceNotFoundException("Person", id);
        if (rentalRepository.existsByPersonId(id))
            throw new ConflictException("Cannot delete a person that is referenced in rentals");
        personRepository.deleteById(id);
    }
}
