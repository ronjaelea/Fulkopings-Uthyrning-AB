package se.gritacademy.fulkopingsuthyrning.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import se.gritacademy.fulkopingsuthyrning.controller.dto.RentalResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RentalModelAssembler implements RepresentationModelAssembler<RentalResponse, EntityModel<RentalResponse>> {

    /**
     * använder HATEOAS för att länka till mer än bara self och collection:
     * <ul>
     * <li>aktuell person och produkt</li>
     * <li>direktlänk till returnRental (endast om det är en aktiv Rental)</li>
     * </ul>
     */
    @Override
    public EntityModel<RentalResponse> toModel(RentalResponse rental) {
        EntityModel<RentalResponse> model = EntityModel.of(
                rental,
                linkTo(methodOn(RentalController.class).get(rental.id())).withSelfRel(),
                linkTo(methodOn(RentalController.class).list()).withRel("collection"),
                linkTo(methodOn(PersonController.class).get(rental.personId())).withRel("person"),
                linkTo(methodOn(ProductController.class).get(rental.productId())).withRel("product")
        );

        if(rental.returnedDate() == null) {
            model.add(linkTo(methodOn(RentalController.class).returnRental(rental.id())).withRel("return"));
        }
        return model;
    }

    @Override
    public CollectionModel<EntityModel<RentalResponse>> toCollectionModel(Iterable<? extends RentalResponse> rentals) {
        return RepresentationModelAssembler.super.toCollectionModel(rentals)
                .add(linkTo(methodOn(RentalController.class).list()).withSelfRel());
    }
}
