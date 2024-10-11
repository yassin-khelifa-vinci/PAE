package be.vinci.pae.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * The ObjectNotFoundException class extends the WebApplicationException class and is used to
 * represent HTTP not found errors (status code 404). This exception is thrown when the requested
 * resource could not be found on the server. This status code is commonly used when the server does
 * not wish to reveal exactly why the request has been refused, or when no other response is
 * applicable.
 */
public class ObjectNotFoundException extends WebApplicationException {

  /**
   * Default constructor. Creates a new instance of ObjectNotFoundException with an HTTP status code
   * of 404 (Not Found).
   */
  public ObjectNotFoundException() {
    super(Status.NOT_FOUND);
  }

  /**
   * Constructor with a message. Creates a new instance of ObjectNotFoundException with the
   * specified message and an HTTP status code of 404 (Not Found).
   *
   * @param message the message of the exception
   */
  public ObjectNotFoundException(String message) {
    super(message, Status.NOT_FOUND);
  }

  /**
   * Constructor with a throwable. Creates a new instance of ObjectNotFoundException with the
   * specified throwable and an HTTP status code of 404 (Not Found). The throwable is used to
   * capture the current state of the stack frames for the current thread.
   *
   * @param throwable the throwable that contains information about the error
   */
  public ObjectNotFoundException(Throwable throwable) {
    super(throwable, Status.NOT_FOUND);
  }
}