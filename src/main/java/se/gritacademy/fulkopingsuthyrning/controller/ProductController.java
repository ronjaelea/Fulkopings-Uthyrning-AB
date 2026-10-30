package se.gritacademy.fulkopingsuthyrning.controller;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.fulkopingsuthyrning.controller.dto.ProductRequest;
import se.gritacademy.fulkopingsuthyrning.model.Product;
import se.gritacademy.fulkopingsuthyrning.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService service;
    private ProductModelAssembler assembler;

    public ProductController(ProductService service, ProductModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("")
    public CollectionModel<EntityModel<Product>> list() {
        return assembler.toCollectionModel(service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> get(@PathVariable Long id) {
        return new ResponseEntity<>(
                assembler.toModel(service.findById(id)),
                HttpStatus.OK
        );
    }

    @PostMapping("")
    public ResponseEntity<EntityModel<Product>> post(@Valid @RequestBody ProductRequest request) {
        EntityModel<Product> model = assembler.toModel(service.create(request));

        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public EntityModel<Product> put(@PathVariable Long id,
                                    @Valid @RequestBody ProductRequest request) {
        return assembler.toModel(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
