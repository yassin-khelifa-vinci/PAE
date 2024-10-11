package be.vinci.pae.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * The ForbiddenException class extends the WebApplicationException class and is used to represent
 * HTTP forbidden errors (status code 403). This exception is thrown when the server understood the
 * request, but it refuses to authorize it. This status code is commonly used when the server does
 * not wish to reveal exactly why the request has been refused, or when no other response is
 * applicable.
 */
public class ForbiddenException extends WebApplicationException {

  /**
   * Default constructor. Creates a new instance of ForbiddenException with an HTTP status code of
   * 403 (Forbidden).
   */
  public ForbiddenException() {
    super(Status.FORBIDDEN);
  }

  /**
   * Constructor with a message. Creates a new instance of ForbiddenException with the specified
   * message and an HTTP status code of 403 (Forbidden).
   *
   * @param message the message of the exception
   */
  public ForbiddenException(String message) {
    super(message, Status.FORBIDDEN);
  }

  /**
   * Constructor with a throwable. Creates a new instance of ForbiddenException with the specified
   * throwable and an HTTP status code of 403 (Forbidden). The throwable is used to capture the
   * current state of the stack frames for the current thread.
   *
   * @param throwable the throwable that contains information
   */
  public ForbiddenException(Throwable throwable) {
    super(throwable, Status.FORBIDDEN);
  }
}