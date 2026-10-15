package se.gritacademy.fulkopingsuthyrning.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.gritacademy.fulkopingsuthyrning.model.Person;
import se.gritacademy.fulkopingsuthyrning.repository.PersonRepository;

import java.util.List;

@Service
@Transactional
public class PersonService {

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> listAll() {
        return personRepository.findAll();
    }

    public Person getById(Long id) {
        if (personRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Person with id " + id + " was not found");
        }
        return personRepository.findById(id).get();
    }

    public void create(Person person) {
        personRepository.save(person);
    }

    // även om id finns med i body skrivs det över av id från path var
    public void update(Person person, Long id) {
        if (personRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Person with id " + id + " was not found");
        }
        person.setId(id);
        personRepository.save(person);
    }

    public void delete(Long id) {
        if (personRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Person with id " + id + " was not found");
        }
        personRepository.deleteById(id);
    }
}
