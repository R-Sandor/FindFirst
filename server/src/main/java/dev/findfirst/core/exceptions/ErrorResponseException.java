package dev.findfirst.core.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class ErrorResponseException extends ResponseStatusException {
  private final String serverLogMessage;

  public ErrorResponseException(HttpStatus code, String reason, String serverLogMessage) {
    super(code, reason);
    this.serverLogMessage = serverLogMessage;
  }

  public ErrorResponseException(HttpStatus code, String reason, String serverLogMessage,
      Throwable cause) {
    super(code, reason, cause);
    this.serverLogMessage = serverLogMessage;
  }
}
