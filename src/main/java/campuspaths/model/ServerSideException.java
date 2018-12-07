package campuspaths.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class defines a intern server exception
 *
 * <p>The @ResponseStatus annotation marks this Error to be an Internal Server Error so that when it
 * is thrown, the API recognizes the error status code. Different exceptions can be created with
 * different response codes including 200 and 404
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerSideException extends Exception {
  // This is NOT An ADT!!!
  // This is NOT An ADT!!!
  // This is NOT An ADT!!!

  /**
   * Constructs an instance of exception with message.
   *
   * @spec.effects: create an instance of exception with message.
   * @param message the message about the exception.
   */
  public ServerSideException(String message) {
    super(message);
  }

  /**
   * Constructs an instance of exception with given exception.
   *
   * @spec.effects: create an instance of exception with given exception.
   * @param e the given exception
   */
  public ServerSideException(Exception e) {
    super(e);
  }
}
