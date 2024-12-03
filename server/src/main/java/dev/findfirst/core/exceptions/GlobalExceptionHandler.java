package dev.findfirst.core.exceptions;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // Handles ResponseStatusException and its child classes
  @ExceptionHandler(value = {ResponseStatusException.class})
  protected ResponseEntity<Map<String, String>> handleResponseStatusException(
      ResponseStatusException ex) {
    // Extract log message
    String logMessage =
        ex instanceof ErrorResponseException ? ((ErrorResponseException) ex).getServerLogMessage()
            : ex.getReason();
    StackTraceElement[] stackTrace = ex.getStackTrace();
    // Get the first relevant stack trace element (usually the point where the exception was thrown)
    StackTraceElement origin = stackTrace.length > 0 ? stackTrace[0] : null;
    if (origin != null) {
      log.error("ResponseStatusException occurred at {}.{} ({}:{}) - Reason: {}",
          origin.getClassName(), origin.getMethodName(), origin.getFileName(),
          origin.getLineNumber(), logMessage);
    } else {
      log.error("ResponseStatusException occurred - Reason: {}", logMessage);
    }

    // Prepare body
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getReason());
    return new ResponseEntity<>(error, ex.getStatusCode());
  }
}
