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
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Map.of() som jag först använde för att skapa maps i handlers kastade NullPointerException
     * om ett rejectedValue var null, dvs ett exception inuti en metod som skulle hantera ett helt annat exception...
     * använder istället ett record för att undvika det
     */
    public record Violation(String field, Object rejectedValue, String message) {
    }

    /** ----- 404 -----*/
    // hanterar eget, custom exception för 404 när id:t inte finns i tabell
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        return problem(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                e.getMessage()
        );
    }

     /**----- 409 -----*/
     // hanterar eget, custom exception för 409 (t.ex produkt redan uthyrd, email redan registrerad)
    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflictException(ConflictException e) {
        return problem(
                HttpStatus.CONFLICT,
                "Conflict Exception",
                e.getMessage()
        );
    }

    // kastas av validering mot databasen. antagligen överflödig sen jag implementerade ConflictException.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return problem(
                HttpStatus.CONFLICT,
                "Data Integrity Violation",
                "The request conflicts with existing data"
        );
    }

    /**----- 400 -----*/
    // kastas om @Valid @RequestBody inte passerar, t.ex matchar ej regex, NotBlank osv
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var pd = problem(
                HttpStatus.BAD_REQUEST,
                "Method Argument Not Valid",
                "Missing or invalid field(s) in request body"
        );
        List<Violation> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> new Violation(
                        fe.getField(),
                        fe.getRejectedValue(),
                        fe.getDefaultMessage()
                ))
                .toList();
        pd.setProperty("errors", errors);

        return pd;
    }

    // kastas om @PathVariable inte är valid (t.ex. @Positive)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        var pd = problem(
                HttpStatus.BAD_REQUEST,
                "Method Validation Exception",
                "Request parameters/path variables are invalid"
        );
        List<Violation> errors = e.getParameterValidationResults().stream()
                .flatMap(r -> r.getResolvableErrors().stream()
                        .map(error -> new Violation(
                                r.getMethodParameter().getParameterName(),
                                r.getArgument(),
                                error.getDefaultMessage())))
                .toList();
        pd.setProperty("errors", errors);

        return pd;
    }

    // kastas av JPA/Hibernate, dvs efter att request passerat/validerats i controller
    // lär sällan bli aktuellt men behåller för säkerhets skull
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException e) {
        var pd = problem(
                HttpStatus.BAD_REQUEST,
                "Constraint Violation",
                "Request parameters/path variables are invalid"
        );
        List<Violation> errors = e.getConstraintViolations().stream()
                .map(cv -> new Violation(
                        cv.getPropertyPath().toString(),
                        cv.getInvalidValue(),
                        cv.getMessage()))
                .toList();
        pd.setProperty("errors", errors);

        return pd;
    }

    /** ----- helpers -----*/
    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setProperty("timestamp", OffsetDateTime.now());

        return pd;
    }
}
