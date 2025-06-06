package be.ucll.campusapp.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {
    public GlobalExceptionHandler() {
        super();
    }

    // ✅ Voor @Valid-fouten (bv. NotBlank, Positive, ...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Map<String, String> err = new HashMap<>();
            err.put("field", error.getField());
            err.put("message", error.getDefaultMessage());
            errors.add(err);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    // ✅ Voor andere gevallen zoals campus bestaat niet, lokaal bestaat al, ...
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 404);
        response.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}

