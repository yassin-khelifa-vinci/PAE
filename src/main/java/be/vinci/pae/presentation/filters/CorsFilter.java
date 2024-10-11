package be.vinci.pae.presentation.filters;

import be.vinci.pae.logger.Log4J;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * This class implements a CORS filter for the API. It adds the necessary CORS headers to the
 * response. The @Provider annotation specifies that this class is a provider of an extension point
 * defined by the JAX-RS API.
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

  /**
   * This method is called for each response to add the necessary CORS headers. It also handles
   * preflight requests by setting the necessary options headers and the response status to OK.
   *
   * @param requestContext  the request context
   * @param responseContext the response context
   */
  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) {
    String logMessage = requestContext.getMethod() + " " + requestContext.getUriInfo()
        .getRequestUri().getPath() + " - " + responseContext.getStatus();
    Log4J.LOGGER.info(logMessage);
    // Add the necessary CORS headers
    responseContext.getHeaders()
        .add("Access-Control-Allow-Origin", "*"); // Adjust this as needed for security
    responseContext.getHeaders()
        .add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    responseContext.getHeaders().add("Access-Control-Allow-Headers",
        "Content-Type, Authorization, X-Requested-With, Accept");
    responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

    // If it's a preflight request, we set the necessary options headers.
    if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
      responseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
      responseContext.setStatus(Response.Status.OK.getStatusCode());
    }
  }
}