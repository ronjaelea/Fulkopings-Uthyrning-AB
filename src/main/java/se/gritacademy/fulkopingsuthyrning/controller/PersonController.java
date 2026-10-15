package se.gritacademy.fulkopingsuthyrning.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.fulkopingsuthyrning.model.Person;
import se.gritacademy.fulkopingsuthyrning.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private PersonService personService;
     //senare: implementera model assembler (för hateoas)

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("")
    public List<Person> list() {
        return personService.listAll();
    }

    @GetMapping("/{id}")
    public Person get(@PathVariable Long id) {
        return personService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity<?> post(@Valid @RequestBody Person person) {
        personService.create(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@Valid @RequestBody Person person,
                                 @PathVariable Long id) {
        personService.update(person, id);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("/{id}")
    @Validated // ??
    public ResponseEntity<?> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
