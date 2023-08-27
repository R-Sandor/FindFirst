package dev.findfirst.bookmarkit.utilies;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
public class Response<T> {

  ResponseEntity<T> resp;

  /**
   * Wrapper constructor for prepareResponse
   *
   * @param action caller defined action.
   * @param t parameter to consumer.
   */
  public Response(Consumer<T> action, T t) {
    prepareResponse(action, t);
  }

  /**
   * Wrapper constructor for prepareResponse
   *
   * @param action caller defined action.
   * @param t parameter to consumer.
   */
  public Response(Consumer<T> action, Optional<T> t) {
    t.ifPresentOrElse(action, () -> this.resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    this.setResponse(t.get(), HttpStatus.OK);
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

  /**
   * Easy prepare Response entity.
   *
   * @param action caller defined action.
   * @param t parameter to consumer.
   */
  public void prepareResponse(Consumer<T> action, T t) {
    this.setResponse(t, HttpStatus.OK);
    action.accept(t);
  }

  /**
   * Prepare response with optional check. Return bad request if there is no data.
   *
   * @param action
   * @param t
   */
  public void prepareResponse(Consumer<T> action, Optional<T> t) {
    t.ifPresentOrElse(action, () -> setResponse(HttpStatus.BAD_REQUEST));
  }

  /**
   * Prepare response with optional check. Return bad request if there is no data.
   *
   * @param t
   */
  public void prepareResponse(Optional<T> t) {
    t.ifPresentOrElse(
        (T lt) -> setResponse(lt, HttpStatus.OK), () -> setResponse(HttpStatus.BAD_REQUEST));
  }

  private void setResponse(T t, HttpStatus status) {
    this.resp = new ResponseEntity<T>(t, status);
  }

  private void setResponse(HttpStatus status) {
    this.resp = new ResponseEntity<T>(status);
  }

  public ResponseEntity<T> getResp() {
    return resp;
  }
}
