package se.gritacademy.fulkopingsuthyrning.service;

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
        return personRepository.findById(id).get();
    }

    public void save(Person person) {
        personRepository.save(person);
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
