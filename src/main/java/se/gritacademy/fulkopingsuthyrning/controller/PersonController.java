package se.gritacademy.fulkopingsuthyrning.controller;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    private PersonModelAssembler personModelAssembler;

    public PersonController(PersonService personService, PersonModelAssembler personModelAssembler) {
        this.personService = personService;
        this.personModelAssembler = personModelAssembler;
    }

    @GetMapping("")
    public CollectionModel<EntityModel<Person>> list() {
        return personModelAssembler.toCollectionModel(personService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Person>> get(@PathVariable Long id) {
        return new ResponseEntity<>(
                personModelAssembler.toModel(personService.getById(id)),
                HttpStatus.OK
        );
    }

    @PostMapping("")
    public ResponseEntity<?> post(@Valid @RequestBody Person person) {
        personService.create(person);
        return new ResponseEntity<>(
                personModelAssembler.toModel(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@Valid @RequestBody Person person,
                                 @PathVariable Long id) {
        personService.update(person, id);
        return new ResponseEntity<>(
                personModelAssembler.toModel(person),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @Validated // ??
    public ResponseEntity<?> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
