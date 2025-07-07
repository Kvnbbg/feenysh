package feenysh.exception;

import feenysh.dto.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle validation errors (e.g., @Valid in DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        String errorMessage = "Validation failed: " + String.join(", ", errors);
        logger.warn("Validation error: {} for request: {}", errorMessage, request.getDescription(false));
        return new ResponseEntity<>(new MessageResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    // Handle general authentication errors (though JwtAuthenticationEntryPoint might catch some)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MessageResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.warn("Authentication error: {} for request: {}", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(new MessageResponse("Authentication failed: " + ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    // Handle authorization errors (e.g., @Secured or method security)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MessageResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        logger.warn("Access denied error: {} for request: {}", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(new MessageResponse("Access Denied: You do not have permission to access this resource."), HttpStatus.FORBIDDEN);
    }

    // Handle specific custom exceptions if you define any
    // For example:
    // @ExceptionHandler(ResourceNotFoundException.class)
    // public ResponseEntity<MessageResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    //     logger.info("Resource not found: {}", ex.getMessage());
    //     return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    // }

    // Handle IllegalArgumentException (used in AuthService for duplicate username/email)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Illegal argument error: {} for request: {}", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // Generic fallback handler for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unhandled exception: {} for request: {}", ex.getMessage(), request.getDescription(false), ex);
        return new ResponseEntity<>(new MessageResponse("An unexpected error occurred: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
