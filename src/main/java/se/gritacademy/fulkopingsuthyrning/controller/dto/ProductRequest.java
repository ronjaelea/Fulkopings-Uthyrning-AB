package se.gritacademy.fulkopingsuthyrning.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotNull(message = "name is required")
        @NotBlank(message = "name cannot be blank")
        String name,

        @NotNull(message = "category is required")
        @NotBlank(message = "category cannot be blank")
        String category,

        @NotNull(message = "pricePerDay is required")
        @Positive(message = "pricePerDay must be a positive integer")
        Integer pricePerDay
) {
}
