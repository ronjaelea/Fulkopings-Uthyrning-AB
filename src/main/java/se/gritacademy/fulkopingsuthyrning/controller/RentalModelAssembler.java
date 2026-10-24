package se.gritacademy.fulkopingsuthyrning.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import se.gritacademy.fulkopingsuthyrning.model.Rental;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RentalModelAssembler implements RepresentationModelAssembler<Rental, EntityModel<Rental>> {

    @Override
    public EntityModel<Rental> toModel(Rental rental) {
        EntityModel<Rental> model = EntityModel.of(
                rental,
                linkTo(methodOn(RentalController.class)
                        .get(rental.getId()))
                        .withSelfRel(),
                linkTo(methodOn(RentalController.class)
                        .list())
                        .withRel("collection"),
                linkTo(methodOn(ProductController.class)
                        .get(rental.getProduct().getId()))
                        .withRel("product")
        );
        return model;

    }

    @Override
    public CollectionModel<EntityModel<Rental>> toCollectionModel(Iterable<? extends Rental> rentals) {
        return RepresentationModelAssembler.super.toCollectionModel(rentals)
                .add(linkTo(methodOn(RentalController.class).list()).withSelfRel());

    }

}
