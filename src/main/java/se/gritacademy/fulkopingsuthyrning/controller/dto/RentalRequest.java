package se.gritacademy.fulkopingsuthyrning.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RentalRequest(

        // @Valid i controllern triggar kontroll här snarare än i entity?
        @NotNull(message = "personId is required")
        @Positive(message = "personId must be a positive integer")
        Long personId,

        @NotNull(message = "productId is required")
        @Positive(message = "productId must be a positive integer")
        Long productId
) {
}
