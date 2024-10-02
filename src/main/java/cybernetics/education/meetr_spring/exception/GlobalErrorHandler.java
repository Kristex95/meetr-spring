package cybernetics.education.meetr_spring.exception;

import cybernetics.education.meetr_spring.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> generalErrorHandler(Exception exception) {
        final String message = "Unexpected error: " + exception.getMessage();
        log.error(message);
        return ResponseEntity.internalServerError().body(Response.message(message));
    }
}
