package be.vinci.pae.exception.mapper;

import be.vinci.pae.logger.Log4J;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Arrays;
import org.apache.logging.log4j.Level;

/**
 * The WebExceptionMapper class implements the ExceptionMapper interface and provides a method for
 * mapping exceptions to HTTP responses. It uses the Log4J logger for logging exceptions.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  /**
   * This method maps an exception to an HTTP response. If the exception is a
   * WebApplicationException, it returns a response with the same status and the exception message
   * as the entity. Otherwise, it returns a response with a status of INTERNAL_SERVER_ERROR and the
   * exception message as the entity. It also logs the exception message and stack trace.
   *
   * @param exception the exception to be mapped
   * @return the HTTP response
   */
  @Override
  public Response toResponse(Throwable exception) {
    String logMessage = "Exception occurred: " + exception.getMessage() + "\n";
    logMessage += "StackTrace:\n" + this.stackTraceToString(exception.getStackTrace());
    Log4J.LOGGER.log(Level.ERROR, logMessage);
    if (exception instanceof WebApplicationException) {
      return Response.status(((WebApplicationException) exception).getResponse().getStatus())
          .entity(exception.getMessage())
          .build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }

  /**
   * This method converts a stack trace to a string. It replaces commas with newlines and removes
   * square brackets to format the stack trace as a multiline string.
   *
   * @param stackTraceElements the stack trace elements to be converted
   * @return the stack trace as a string
   */
  private String stackTraceToString(StackTraceElement[] stackTraceElements) {
    return Arrays.toString(stackTraceElements)
        .replace(",", "\n")
        .replace("[", "")
        .replace("]", "");
  }
}