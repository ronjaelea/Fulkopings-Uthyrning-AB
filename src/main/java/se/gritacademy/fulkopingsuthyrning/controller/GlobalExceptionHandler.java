package se.gritacademy.fulkopingsuthyrning.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // triggas om t.ex id inte finns i tabell
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // triggas om @Valid @RequestBody inte är valid, t.ex matchar ej regex, längd osv
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
//        pd.setDetail(e.getMessage());
        var errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "rejectedValue", fe.getRejectedValue(),
                        "message", fe.getDefaultMessage()
                ))
                .toList();
        pd.setProperty("errors", errors);
        pd.setProperty("timestamp", OffsetDateTime.now());

        return pd;
    }

    // triggas av JPA/Hibernate, dvs efter att request passerat/validerats i controller
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(
            ConstraintViolationException e
    ) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Constraint Violation");
        pd.setDetail("Request parameters/path variables are invalid");
        var error = e.getConstraintViolations().stream()
                .map( cv -> Map.of(
                        "property", cv.getPropertyPath().toString(),
                        "invalidValue", cv.getInvalidValue().toString(),
                        "message", cv.getMessage()
                ))
                .toList();
        pd.setProperty("errors", error);
        pd.setProperty("timestamp", OffsetDateTime.now());
        return pd;
    }

    // (ofärdig/last resort) triggas av t.ex. unique, not-null, foreign key violation. lite för ospecifik
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        var pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Data Integrity Violation");
        pd.setDetail(e.getMessage());

        return pd;
    }
}