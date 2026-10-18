package se.gritacademy.fulkopingsuthyrning.controller;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> post(@Valid @RequestBody Product product) {
        service.create(product);
        return new ResponseEntity<>(
                assembler.toModel(product),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@Valid @RequestBody Product product,
                                 @PathVariable Long id) {
        service.update(product, id);
        return new ResponseEntity<>(
                assembler.toModel(product),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
