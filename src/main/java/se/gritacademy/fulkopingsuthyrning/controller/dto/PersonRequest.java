package se.gritacademy.fulkopingsuthyrning.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PersonRequest(
        @NotNull(message = "firstName is required")
        @NotBlank(message = "firstName cannot be blank")
        String firstName,

        @NotNull(message = "lastName is required")
        @NotBlank(message = "lastName cannot be blank")
        String lastName,

        @NotNull(message = "email is required")
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "Make sure you've entered a correct email address"
        )
        String email
) {
}
