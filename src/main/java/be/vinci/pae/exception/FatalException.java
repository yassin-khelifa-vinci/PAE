package be.vinci.pae.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * This class represents a fatal exception in the system. It extends the WebApplicationException
 * class, which is a runtime exception that can be thrown to return a specific HTTP status response.
 * The FatalException class is used to indicate that a serious error has occurred that should result
 * in the termination of the current operation and possibly the entire application.
 */
public class FatalException extends WebApplicationException {

  /**
   * Default constructor. Creates a new instance of FatalException with an HTTP status code of 500
   * (Internal Server Error).
   */
  public FatalException() {
    super(Status.INTERNAL_SERVER_ERROR);
  }

  /**
   * Constructor with a message. Creates a new instance of FatalException with the specified message
   * and an HTTP status code of 500 (Internal Server Error).
   *
   * @param message the message of the exception
   */
  public FatalException(String message) {
    super(message, Status.INTERNAL_SERVER_ERROR);
  }

  /**
   * Constructor with a throwable. Creates a new instance of FatalException with the specified
   * throwable and an HTTP status code of 500 (Internal Server Error). The throwable is used to
   * capture the current state of the stack frames for the current thread.
   *
   * @param throwable the throwable that contains information
   */
  public FatalException(Throwable throwable) {
    super(throwable.getMessage(), throwable, Status.INTERNAL_SERVER_ERROR);
  }

}