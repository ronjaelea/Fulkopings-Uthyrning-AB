package se.gritacademy.fulkopingsuthyrning.controller;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.fulkopingsuthyrning.controller.dto.PersonRequest;
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
    public ResponseEntity<EntityModel<Person>> post(@Valid @RequestBody PersonRequest request) {
        EntityModel<Person> model = assembler.toModel(service.create(request));

        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public EntityModel<Person> put(@PathVariable Long id,
                                   @Valid @RequestBody PersonRequest request) {
        return assembler.toModel(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
