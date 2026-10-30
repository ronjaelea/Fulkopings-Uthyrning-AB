package se.gritacademy.fulkopingsuthyrning.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import se.gritacademy.fulkopingsuthyrning.model.Person;
import se.gritacademy.fulkopingsuthyrning.model.Rental;
import se.gritacademy.fulkopingsuthyrning.repository.RentalRepository;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PersonModelAssembler implements RepresentationModelAssembler<Person, EntityModel<Person>> {

    // borde inte ta in något repository här men ville testa
    // för att få in länkar till aktiva lån
    private final RentalRepository rentalRepository;

    public PersonModelAssembler(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public EntityModel<Person> toModel(Person person) {
        List<Rental> rentals = rentalRepository.findByPersonIdAndReturnedDateIsNull(person.getId());

        EntityModel<Person> model = EntityModel.of(
                person,
                linkTo(methodOn(PersonController.class)
                        .get(person.getId()))
                        .withSelfRel(),
                linkTo(methodOn(PersonController.class)
                        .list())
                        .withRel("collection")
        );

        for (Rental rental : rentals) {
            model.add(linkTo(methodOn(RentalController.class).get(rental.getId())).withRel("active rentals"));
        }
        return model;
    }

    @Override
    public CollectionModel<EntityModel<Person>> toCollectionModel(Iterable<? extends Person> persons) {
        return RepresentationModelAssembler.super.toCollectionModel(persons)
                .add(linkTo(methodOn(PersonController.class).list()).withSelfRel());
    }
}
