package se.gritacademy.fulkopingsuthyrning.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import se.gritacademy.fulkopingsuthyrning.exception.ConflictException;
import se.gritacademy.fulkopingsuthyrning.exception.ResourceNotFoundException;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------- 404 ----------

    // hanterar eget, custom exception för 404 när id:t inte finns i tabell
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        return problem(
                HttpStatus.NOT_FOUND,
                "Resource not found",
                e.getMessage()
        );
    }

    // ---------- 409 ----------

    // hanterar eget, custom exception för 409 (t.ex produkt redan uthyrd, email redan registrerad)
    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflictException(ConflictException e) {
        return problem(
                HttpStatus.CONFLICT,
                "Conflict exception",
                e.getMessage()
        );
    }

        /**
         * triggas av validering t.ex. unique, not-null...
         * e.getMessage() läckte sql-detaljer, skickar en mer allmän string som detail istället –
         * de mest väntade konflikterna (email ej unik, produkt redan uthyrd)
         * hanteras ändå av min egen ConflictException som ger lite mer detaljerad feedback
         * från service
         */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return problem(
                HttpStatus.CONFLICT,
                "Data Integrity Violation",
                "The request conflicts with existing data"
        );
    }

    // ---------- 400 ----------

    // triggas om @Valid @RequestBody inte passerar, t.ex matchar ej regex, NotBlank osv
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var pd = problem(
                HttpStatus.BAD_REQUEST,
                "Method argument not valid",
                "Invalid field(s) in request body"
        );
        var errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "rejectedValue", fe.getRejectedValue(),
                        "message", fe.getDefaultMessage()
                ))
                .toList();
        pd.setProperty("errors", errors);

        return pd;
    }

    // triggas av JPA/Hibernate, dvs efter att request passerat/validerats i controller
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException e) {
        var pd = problem(
                HttpStatus.BAD_REQUEST,
                "Constrain Violation Exception",
                "Request parameters/path variables are invalid"
        );
        var errors = e.getConstraintViolations().stream()
                .map( cv -> Map.of(
                        "property", cv.getPropertyPath().toString(),
                        "invalidValue", cv.getInvalidValue().toString(),
                        "message", cv.getMessage()
                ))
                .toList();
        pd.setProperty("errors", errors);
        pd.setProperty("timestamp", OffsetDateTime.now());

        return pd;
    }

    // triggas om @PathVariable inte är valid (har inga såna checks i nuläget)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        var pd = problem(
                HttpStatus.BAD_REQUEST,
                "Method Validation Exception",
                "Request parameters/path variables are invalid"
        );
        var errors = e.getParameterValidationResults().stream()
                .flatMap( r -> r.getResolvableErrors().stream()
                        .map( err -> Map.of(
                                "parameter", r.getMethodParameter().getParameterName(),
                                "invalidValue", String.valueOf(r.getArgument()),
                                "message", err.getDefaultMessage()
                        )))
                .toList();
        pd.setProperty("errors", errors);

        return pd;
    }

    // hjälpmetod, gör handlers mer simpla och enhetliga
    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setProperty("timestamp", OffsetDateTime.now());

        return pd;
    }
}
