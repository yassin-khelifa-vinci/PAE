package be.vinci.pae.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * The ConflictException class extends the WebApplicationException class and is used to represent
 * HTTP conflict errors (status code 409). This exception is thrown when a request could not be
 * completed due to a conflict with the current state of the target resource.
 */
public class ConflictException extends WebApplicationException {

  /**
   * Default constructor. Creates a new instance of ConflictException with an HTTP status code of
   * 409 (Conflict).
   */
  public ConflictException() {
    super(Status.CONFLICT);
  }

  /**
   * Constructor with a message. Creates a new instance of ConflictException with the specified
   * message and an HTTP status code of 409 (Conflict).
   *
   * @param message the message of the exception
   */
  public ConflictException(String message) {
    super(message, Status.CONFLICT);
  }

  /**
   * Constructor with a throwable. Creates a new instance of ConflictException with the specified
   * throwable and an HTTP status code of 409 (Conflict). The throwable is used to capture the
   * current state of the stack frames for the current thread.
   *
   * @param throwable the throwable that contains information
   */
  public ConflictException(Throwable throwable) {
    super(throwable, Status.CONFLICT);
  }
}