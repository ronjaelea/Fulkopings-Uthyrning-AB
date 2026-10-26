package se.gritacademy.fulkopingsuthyrning.controller;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.fulkopingsuthyrning.controller.dto.RentalRequest;
import se.gritacademy.fulkopingsuthyrning.controller.dto.RentalResponse;
import se.gritacademy.fulkopingsuthyrning.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService service;
    private final RentalModelAssembler assembler;

    public RentalController(RentalService service, RentalModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("")
    public CollectionModel<EntityModel<RentalResponse>> list() {
        return assembler.toCollectionModel(service.listAll());
    }

    @GetMapping("/{id}")
    public EntityModel<RentalResponse> get(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<EntityModel<RentalResponse>> create(@Valid @RequestBody RentalRequest request) {
        EntityModel<RentalResponse> model = assembler.toModel(service.create(request));
        return ResponseEntity
                // .created() för statuskod & location header (bör finnas med vid 201)
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PostMapping("/{id}/return")
    public EntityModel<RentalResponse> returnRental(@PathVariable Long id) {
        return assembler.toModel(service.returnRental(id));
    }
}
