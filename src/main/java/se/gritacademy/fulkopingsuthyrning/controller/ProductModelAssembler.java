package se.gritacademy.fulkopingsuthyrning.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import se.gritacademy.fulkopingsuthyrning.model.Person;
import se.gritacademy.fulkopingsuthyrning.model.Product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public EntityModel<Product> toModel(Product product) {
        EntityModel<Product> model = EntityModel.of(
                product,
                linkTo(methodOn(ProductController.class)
                        .get(product.getId()))
                        .withSelfRel(),
                linkTo(methodOn(ProductController.class)
                        .list())
                        .withRel("collection")
        );
        return model;
    }

    @Override
    public CollectionModel<EntityModel<Product>> toCollectionModel(Iterable<? extends Product> products) {
        return RepresentationModelAssembler.super.toCollectionModel(products);
    }

}
