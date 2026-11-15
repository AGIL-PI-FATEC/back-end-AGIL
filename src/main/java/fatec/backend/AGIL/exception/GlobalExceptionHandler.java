package fatec.backend.AGIL.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String msg = ex.getMessage();
        Map<String, String> error = new HashMap<>();

        if (msg != null && msg.contains("não encontrado")) {
            error.put("erro", msg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        if (msg != null && (msg.contains("já está cadastrado") || msg.contains("já está sendo utilizado"))) {
            error.put("erro", msg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        error.put("erro", msg != null ? msg : "Erro interno");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}