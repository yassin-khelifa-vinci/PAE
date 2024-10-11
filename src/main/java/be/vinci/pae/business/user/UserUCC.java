package be.vinci.pae.business.user;

import be.vinci.pae.exception.FatalException;
import java.util.List;

/**
 * The UserUCC interface provides the structure for a Use Case Controller (UCC) that handles the
 * business operations related to User entities. It defines methods to authenticate a user, retrieve
 * a user by their id, and to register a new user.
 */
public interface UserUCC {

  /**
   * This method is used to authenticate a user. It takes the user's email and password as
   * parameters and returns a UserDTO object representing the authenticated user. If the
   * authentication fails, it returns null.
   *
   * @param email    The email of the user to authenticate.
   * @param password The password of the user to authenticate.
   * @return A UserDTO object representing the authenticated user. If the authentication fails, it
   *     returns null.
   */
  UserDTO login(String email, String password);

  /**
   * This method is used to retrieve a user by their id from the database. It takes the user's id as
   * a parameter and returns a UserDTO object representing the user with the given id. If no user is
   * found, it returns null.
   *
   * @param id The id of the user to retrieve.
   * @return A UserDTO object representing the user with the given id. If no user is found, it
   *     returns null.
   */
  UserDTO refreshUser(int id);

  /**
   * This method is used to register a new user. It takes a UserDTO object containing the user data
   * as a parameter and returns a UserDTO object representing the created user. If the user
   * registration fails, it throws a RuntimeException.
   *
   * @param user The UserDTO object containing the user data.
   * @return The created UserDTO object. If the user registration fails, it throws a
   *     RuntimeException.
   */
  UserDTO register(UserDTO user);

  /**
   * This method is used to retrieve a user from the database by their email. It takes the user's
   * email as a parameter and returns a UserDTO object representing the user with the given email.
   *
   * @param user          The UserDTO object containing the user data.
   * @param password      The password of the user to authenticate.
   * @param newPassword   The new password of the user.
   * @param versionNumber The version number of the user.
   * @return The created UserDTO object. If the user registration fails, it throws a
   *     RuntimeException.
   */
  UserDTO editData(UserDTO user, String password, String newPassword, int versionNumber);

  /**
   * Retrieves all users from the database.
   *
   * @return A list of UserDTO objects representing all users.
   * @throws FatalException if a database access error occurs.
   */
  List<UserDTO> getAllUsers();


}