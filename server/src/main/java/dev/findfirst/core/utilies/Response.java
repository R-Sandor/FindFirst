package dev.findfirst.core.utilies;

import java.util.Optional;
import java.util.function.Consumer;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class Response<T> {

  ResponseEntity<T> resp;

  /**
   * Wrapper constructor for prepareResponse
   *
   * @param action caller defined action.
   * @param t parameter to consumer.
   */
  public Response(Consumer<T> action, Optional<T> t) {
    t.ifPresentOrElse(action, () -> this.resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    this.setResponse(t.orElseThrow(), HttpStatus.OK);
  }

  /**
   * Body and status
   *
   * @param t body of request.
   * @param status HttpStatus
   */
  public Response(T t, HttpStatus status) {
    setResponse(t, status);
  }

  public Response(Optional<T> t) {
    prepareResponse(t);
  }

  /**
   * Prepare response with optional check. Return bad request if there is no data.
   *
   * @param t
   */
  public ResponseEntity<T> prepareResponse(Optional<T> t) {
    t.ifPresentOrElse((T lt) -> setResponse(lt, HttpStatus.OK),
        () -> setResponse(HttpStatus.BAD_REQUEST));
    return this.resp;
  }

  public ResponseEntity<T> setResponse(T t, HttpStatus status) {
    this.resp = new ResponseEntity<T>(t, status);
    return resp;
  }

  public ResponseEntity<T> setResponse(HttpStatus status) {
    this.resp = new ResponseEntity<T>(status);
    return resp;
  }

  public ResponseEntity<T> get() {
    return resp;
  }
}
