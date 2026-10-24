package se.gritacademy.fulkopingsuthyrning.controller;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.fulkopingsuthyrning.model.Product;
import se.gritacademy.fulkopingsuthyrning.model.Rental;
import se.gritacademy.fulkopingsuthyrning.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private RentalService service;
    private RentalModelAssembler assembler;

    public RentalController(RentalService service, RentalModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("")
    public CollectionModel<EntityModel<Rental>> list() {
        return assembler.toCollectionModel(service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Rental>> get(@PathVariable Long id) {
        return new ResponseEntity<>(
                assembler.toModel(service.findById(id)),
                HttpStatus.OK
        );
    }

    @PostMapping("")
    public ResponseEntity<?> post(@Valid @RequestBody Long personId, Long productId) {

        // return denial/conflict if product currently rented out (error thrown in service)

        service.newRental(personId, productId);
        return new ResponseEntity<>(
                assembler.toModel(service.newRental(personId, productId)),
                HttpStatus.CREATED
        );
    }

}
