package se.gritacademy.fulkopingsuthyrning.controller;

import org.springframework.web.bind.annotation.*;
import se.gritacademy.fulkopingsuthyrning.model.Person;
import se.gritacademy.fulkopingsuthyrning.service.PersonService;

@RestController
@RequestMapping("/api")
public class PersonController {

    private PersonService personService;
     //senare: implementera model assembler (för hateoas)

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons/{id}")
    public Person getPerson(@PathVariable Long id) {
        return personService.getById(id);
    }

    @PostMapping("/persons")
    public void savePerson(@RequestBody Person person) {
        personService.save(person);
    }
}
