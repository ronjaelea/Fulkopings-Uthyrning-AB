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

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private PersonService service;
    private PersonModelAssembler assembler;

    public PersonController(PersonService service, PersonModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("")
    public CollectionModel<EntityModel<Person>> list() {
        return assembler.toCollectionModel(service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Person>> get(@PathVariable Long id) {
        return new ResponseEntity<>(
                assembler.toModel(service.findById(id)),
                HttpStatus.OK
        );
    }

    @PostMapping("")
    public ResponseEntity<?> post(@Valid @RequestBody Person person) {
        return new ResponseEntity<>(
                assembler.toModel(service.create(person)),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@Valid @RequestBody Person person,
                                 @PathVariable Long id) {
        return new ResponseEntity<>(
                assembler.toModel(service.update(person, id)),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
