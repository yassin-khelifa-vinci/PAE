package be.vinci.pae.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * The UnauthorizedException class extends the WebApplicationException class and is used to
 * represent HTTP unauthorized errors (status code 401). This exception is thrown when the request
 * requires user authentication. This status code is commonly used when the server does not wish to
 * reveal exactly why the request has been refused, or when no other response is applicable.
 */
public class UnauthorizedException extends WebApplicationException {

  /**
   * Default constructor. Creates a new instance of UnauthorizedException with an HTTP status code
   * of 401 (Unauthorized).
   */
  public UnauthorizedException() {
    super(Status.UNAUTHORIZED);
  }

  /**
   * Constructor with a message. Creates a new instance of UnauthorizedException with the specified
   * message and an HTTP status code of 401 (Unauthorized).
   *
   * @param message the message of the exception
   */
  public UnauthorizedException(String message) {
    super(message, Status.UNAUTHORIZED);
  }

  /**
   * Constructor with a throwable. Creates a new instance of UnauthorizedException with the
   * specified throwable and an HTTP status code of 401 (Unauthorized). The throwable is used to
   * capture the current state of the stack frames for the current thread.
   *
   * @param throwable the throwable that contains information about the error
   */
  public UnauthorizedException(Throwable throwable) {
    super(throwable, Status.UNAUTHORIZED);
  }
}