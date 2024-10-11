package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.business.user.UserUCC;
import be.vinci.pae.exception.TokenDecodingException;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This class implements the ContainerRequestFilter interface and provides a method for filtering
 * incoming HTTP requests based on the user's role. It uses the JWT (JSON Web Token) for
 * authentication and authorization.
 */
@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  // The algorithm used for JWT token verification
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));

  // The verifier for JWT tokens
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();

  // The service for user data
  @Inject
  private UserUCC myUserUcc;

  // The resource information for the current request
  @Context
  private ResourceInfo resourceInfo;

  /**
   * This method filters incoming requests. It checks for the presence of an Authorization token in
   * the request header. If the token is not present, it aborts the request with a UNAUTHORIZED
   * response. If the token is present, it verifies the token and retrieves the user information. If
   * the token is invalid or the user does not exist, it aborts the request with a FORBIDDEN
   * response. If the token is valid and the user exists, it sets the user as a property in the
   * request context.
   *
   * @param requestContext the context of the incoming request
   */
  @Override
  public void filter(ContainerRequestContext requestContext) {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
          .entity("You are not authorized to access this resource").build());
    } else {
      // Verify the token
      DecodedJWT decodedToken = verifyToken(token);
      // Get the user's data
      UserDTO authenticatedUser = myUserUcc.refreshUser(decodedToken.getClaim("user").asInt());
      // Get the user's role
      Role userRole = authenticatedUser.getRole();

      // Get the required role from the @Authorize annotation
      Method method = resourceInfo.getResourceMethod();
      Role[] requiredRoles = method.getAnnotation(Authorize.class).value();

      // Check if the user's role matches the required role
      if (!Arrays.asList(requiredRoles).contains(userRole)) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("You do not have the required role to access this resource").build());
      }

      requestContext.setProperty("user", authenticatedUser);
    }
  }

  /**
   * This method verifies the JWT token. If the token cannot be verified, it throws a
   * TokenDecodingException.
   *
   * @param token the JWT token to be verified
   * @return the decoded JWT token
   */
  private DecodedJWT verifyToken(String token) {
    try {
      return this.jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new TokenDecodingException(e);
    }
  }
}