package be.vinci.pae.dal.user;

import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.exception.FatalException;
import java.util.List;

/**
 * The UserDAO interface provides the structure for a Data Access Object (DAO) that handles the data
 * operations related to User entities. It defines methods to retrieve a user by their email and id,
 * and to create a new user.
 */
public interface UserDAO {

  /**
   * This method retrieves a user by their email from the database.
   *
   * @param email The email of the user to retrieve.
   * @return A UserDTO object representing the user with the given email. If no user is found, it
   *     returns null.
   */
  UserDTO getOneByEmail(String email);

  /**
   * This method retrieves a user by their id from the database.
   *
   * @param id The id of the user to retrieve.
   * @return A UserDTO object representing the user with the given id. If no user is found, it
   *     returns null.
   */
  UserDTO getOneById(int id);

  /**
   * This method creates a new user in the database.
   *
   * @param user The UserDTO object containing the user data.
   * @return The created UserDTO object. If the user creation fails, it throws a RuntimeException.
   */
  UserDTO createOne(UserDTO user);

  /**
   * This method creates a new user in the database.
   *
   * @return A list of UserDTO objects representing all users.
   * @throws FatalException if a database access error occurs.
   */
  List<UserDTO> getAll();

  /**
   * This method edits the data of the user in the database.
   *
   * @param user The UserDTO object containing the updated user data.
   * @return The updated UserDTO object. If the update operation fails, it should handle the failure
   *     according to the application's error handling strategy.
   */
  UserDTO updateOne(UserDTO user);

}