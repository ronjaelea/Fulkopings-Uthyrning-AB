package se.gritacademy.fulkopingsuthyrning.controller.dto;

import se.gritacademy.fulkopingsuthyrning.model.Rental;
import java.time.Instant;

/** <h6>response DTO för att:</h6>
 * med FetchType.LAZY är t.ex. rental.getPerson() inte ett riktigt Person-objekt,
 * Jackson kan inte hantera det(?).
 * till skillnad från entiteten har DTO:n bara enskilda värden (id i det här fallet),
 * inte representationer av hela objekten för Person & Product.
 * för Person & Product behövs alltså ingen response DTO eftersom de inte har ngn ManyToOne
 * <p>boolean active överflödig i entity men användbar här för att klienten
 * ska slippa ha egen logik för att checka returnedDate == null...
 * bra exempel på hur dto kan skilja sig från entity?</p>
 */
public record RentalResponse(
        Long id,
        Long personId,
        Long productId,
        Instant rentalDate,
        Instant returnedDate,
        Integer totalCost,
        boolean active
) {

    // använder static factory ist för konstruktor för att instansiera i service
    public static RentalResponse from(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getPerson().getId(),
                rental.getProduct().getId(),
                rental.getRentalDate(),
                rental.getReturnedDate(),
                rental.getTotalCost(),
                rental.getReturnedDate() == null
        );
    }
}
