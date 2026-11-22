package fatec.backend.AGIL.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

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
        return erro(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Regra de negócio que impede a operação no estado atual (ex.: gestor com funcionários vinculados)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        return erro(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Exceções com status explícito (usadas no módulo de produtos)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(ResponseStatusException ex) {
        String msg = ex.getReason() != null ? ex.getReason() : ex.getStatusCode().toString();
        return erro(HttpStatus.valueOf(ex.getStatusCode().value()), msg);
    }

    // Violação de restrição do banco (chave única, chave estrangeira etc.) sem expor o SQL
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return erro(HttpStatus.CONFLICT, "Operação viola uma restrição de integridade dos dados.");
    }

    // JSON malformado ou com tipo de dado incorreto no corpo
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleNotReadable(HttpMessageNotReadableException ex) {
        return erro(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou mal formatado.");
    }

    // Parâmetro de URL com tipo errado (ex.: /gestores/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return erro(HttpStatus.BAD_REQUEST, "Valor inválido para o parâmetro '" + ex.getName() + "'.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String msg = ex.getMessage();

        if (msg != null && msg.contains("não encontrado")) {
            return erro(HttpStatus.NOT_FOUND, msg);
        }

        if (msg != null && msg.contains("Credenciais inválidas")) {
            return erro(HttpStatus.UNAUTHORIZED, msg);
        }

        if (msg != null && (msg.contains("já está cadastrado") || msg.contains("já está sendo utilizado"))) {
            return erro(HttpStatus.CONFLICT, msg);
        }

        return erro(HttpStatus.BAD_REQUEST, msg != null ? msg : "Erro interno");
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", mensagem);
        return ResponseEntity.status(status).body(error);
    }
}
