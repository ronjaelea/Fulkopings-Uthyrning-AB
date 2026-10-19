package se.gritacademy.fulkopingsuthyrning.service;

import jakarta.persistence.EntityNotFoundException;
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

    public Person findById(Long id) {
        if (!personExists(id))
            throw new EntityNotFoundException("Person with id " + id + " was not found");
        return personRepository.findById(id).get();
    }

    public Person create(Person person) {
    /*
         id får inte tas med om det skulle komma med från body
         då blir existerande rad overwritten
         fixa helst med DTO
    */
        return personRepository.save(person);
    }

    public Person update(Person person, Long id) {
        if (!personExists(id))
            throw new EntityNotFoundException("Person with id " + id + " was not found");
        person.setId(id); // även om id finns med i body skrivs det över av id från path var
        return personRepository.save(person);
    }

    public void delete(Long id) {
        if(!personExists(id))
            throw new EntityNotFoundException("Person with id " + id + " was not found");
        personRepository.deleteById(id);
    }

    private boolean personExists(Long id) {
        return personRepository.findById(id).isPresent();
    }
}
