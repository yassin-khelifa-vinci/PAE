package be.vinci.pae.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * The WrongBodyDataException class extends the WebApplicationException class and is used to
 * represent HTTP bad request errors (status code 400). This exception is thrown when the server
 * could not understand the request due to invalid syntax. This status code is commonly used when
 * the server does not wish to reveal exactly why the request has been refused, or when no other
 * response is applicable.
 */
public class WrongBodyDataException extends WebApplicationException {

  /**
   * Default constructor. Creates a new instance of WrongBodyDataException with an HTTP status code
   * of 400 (Bad Request).
   */
  public WrongBodyDataException() {
    super(Status.BAD_REQUEST);
  }

  /**
   * Constructor with a message. Creates a new instance of WrongBodyDataException with the specified
   * message and an HTTP status code of 400 (Bad Request).
   *
   * @param message the message of the exception
   */
  public WrongBodyDataException(String message) {
    super(message, Status.BAD_REQUEST);
  }

  /**
   * Constructor with a throwable. Creates a new instance of WrongBodyDataException with the
   * specified throwable and an HTTP status code of 400 (Bad Request). The throwable is used to
   * capture the current state of the stack frames for the current thread.
   *
   * @param throwable the throwable that contains information about the error
   */
  public WrongBodyDataException(Throwable throwable) {
    super(throwable, Status.BAD_REQUEST);
  }
}