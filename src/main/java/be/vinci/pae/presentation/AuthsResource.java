package be.vinci.pae.presentation;

import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.business.user.UserUCC;
import be.vinci.pae.exception.FatalException;
import be.vinci.pae.exception.WrongBodyDataException;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * This class provides the API endpoints for authentication. It includes methods for user login and
 * refreshing a user token. The @Singleton annotation specifies that only one instance of this class
 * should be created. The @Path annotation specifies the base URI for the resource class.
 */
@Singleton
@Path("/auths")
public class AuthsResource {

  // JWT algorithm instance
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  // Token duration in hours
  private final int tokenDuration = Config.getIntProperty("tokenExpiration");

  // ObjectMapper instance for JSON processing
  private final ObjectMapper objectMapper = new ObjectMapper();

  // Regular expression for email validation
  private final String emailRegex =
      "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*\\.[a-zA-Z0-9_+&*-]+@(?:student\\.vinci\\"
          + ".be|vinci\\.be)$";

  // Pattern instance for email validation
  private final Pattern emailPattern = Pattern.compile(emailRegex);

  // The service for user data
  @Inject
  private UserUCC myUserUcc;

  /**
   * This method provides the API endpoint for user login. It checks the provided credentials and
   * returns the user data if the login is successful. If the login or password is not provided or
   * incorrect, it throws a WebApplicationException with an appropriate response.
   *
   * @param json the JSON object containing the login credentials
   * @return the user data if the login is successful
   * @throws WebApplicationException with BAD_REQUEST or UNAUTHORIZED status if login fails
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("email") || !json.hasNonNull("password")) {
      throw new WrongBodyDataException("Email or password required");
    }
    String email = json.get("email").asText();
    String password = json.get("password").asText();

    // Validate email and password
    if (!emailPattern.matcher(email).matches() || password.isBlank()) {
      throw new WrongBodyDataException("Invalid email or password");
    }

    // Try to login
    UserDTO publicUser = myUserUcc.login(email, password);

    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.set("user", objectMapper.valueToTree(publicUser));
    objectNode.put("token", createToken(publicUser.getIdUser()));
    // Return the user data as a JSON object
    return objectNode;
  }

  /**
   * This method provides the API endpoint for refreshing a user token. It retrieves the
   * authenticated user from the request and returns a JSON object representing the refreshed user.
   *
   * @param request the ContainerRequest to retrieve the authenticated user
   * @return a JSON object representing the refreshed user
   */
  @GET
  @Path("refresh")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDTO refresh(@Context ContainerRequest request) {
    // Get the user from the request
    return (UserDTO) request.getProperty("user");
  }

  /**
   * This method provides the API endpoint for user registration. It checks the provided user data
   * and registers the user if the data is valid. If the data is not valid or the user already
   * exists, it throws a WebApplicationException with an appropriate response.
   *
   * @param user the UserDTO object containing the user data
   * @return the registered user data if the registration is successful
   * @throws WebApplicationException with BAD_REQUEST or CONFLICT status if registration fails
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode register(UserDTO user) {
    // Get and check user data
    if (user == null || Util.checkEmptyString(user.getPassword()) || Util.checkEmptyString(
        user.getFirstName()) || Util.checkEmptyString(user.getEmail())
        || Util.checkEmptyString(user.getLastName()) || Util.checkEmptyString(
        user.getPhoneNumber())) {
      throw new WrongBodyDataException("all infos required");
    }
    if (!emailPattern.matcher(user.getEmail()).matches()) {
      throw new WrongBodyDataException("Invalid email");
    }
    if (user.getRole() == null) {
      throw new WrongBodyDataException("Correct Role is required");
    }
    // Try to register
    UserDTO publicUser = myUserUcc.register(user);

    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.set("user", objectMapper.valueToTree(publicUser));
    objectNode.put("token", createToken(publicUser.getIdUser()));
    // Return the user data as a JSON object
    return objectNode;
  }

  /**
   * Retrieves all users from the database.
   *
   * @return A list of UserDTO objects representing all users.
   * @throws FatalException if a database access error occurs.
   */
  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize({Role.ADMINISTRATIVE, Role.TEACHER})
  public List<UserDTO> getAllUsers() {
    return myUserUcc.getAllUsers();
  }

  /**
   * This method creates a JWT token for the user with the given id. It sets the issuer to "auth0",
   * the "user" claim to the user id, and the expiration date to the current time plus the token
   * duration. If unable to create the token, it throws a WebApplicationException with
   * INTERNAL_SERVER_ERROR status.
   *
   * @param id the id of the user
   * @return the created JWT token
   * @throws WebApplicationException with INTERNAL_SERVER_ERROR status if unable to create the
   *                                 token
   */
  private String createToken(int id) {
    try {
      Date date = new Date();
      long tokenDurationMillis = TimeUnit.HOURS.toMillis(tokenDuration);

      // Create a JWT token
      String token = JWT.create()
          .withIssuer("auth0")
          .withClaim("user", id)
          .withExpiresAt(new Date(date.getTime() + tokenDurationMillis))
          .sign(this.jwtAlgorithm);

      // Return the created token
      return token;

    } catch (Exception e) {
      // Log the error and throw a WebApplicationException if unable to create the token
      System.out.println("Unable to create token");
      throw new WebApplicationException("Unable to create token",
          Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This method provides the API endpoint for editing a user's data. It takes a UserDTO object as a
   * parameter, which contains the user data to be updated. The method first validates the email in
   * the UserDTO object. If the email is not valid, it throws a WrongBodyDataException. If the email
   * is valid, it calls the editData method of the UserUCC service to update the user data in the
   * database. The method then returns a JSON object representing the updated user.
   *
   * @param request The ContainerRequest to retrieve the authenticated user.
   * @param json    The JSON object containing the updated user data.
   * @return A JSON object representing the updated user.
   * @throws WrongBodyDataException If the email in the UserDTO object is not valid.
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDTO edit(JsonNode json, @Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");
    String phoneNumber = json.has("phoneNumber") ? json.get("phoneNumber").asText() : null;
    String currentPassword =
        json.has("currentPassword") ? json.get("currentPassword").asText() : null;
    String newPassword = json.has("newPassword") ? json.get("newPassword").asText() : null;
    int versionNumber = json.has("versionNumber") ? json.get("versionNumber").asInt() : -1;
    if (versionNumber < 1) {
      throw new WrongBodyDataException("Version number is incorrect");
    }
    if (phoneNumber == null && currentPassword == null && newPassword == null) {
      throw new WrongBodyDataException("No data to update");
    }
    if (phoneNumber != null) {
      if (phoneNumber.isBlank()) {
        throw new WrongBodyDataException("Phone number cannot be empty");
      }
      user.setPhoneNumber(phoneNumber);
    }
    if (currentPassword != null && currentPassword.isBlank()) {
      throw new WrongBodyDataException("Current password cannot be empty");
    }
    if (newPassword != null && newPassword.isBlank()) {
      throw new WrongBodyDataException("New password cannot be empty");
    }
    // Return the user data
    return myUserUcc.editData(user, currentPassword, newPassword, versionNumber);
  }


}