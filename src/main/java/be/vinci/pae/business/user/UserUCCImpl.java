package be.vinci.pae.business.user;

import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.user.UserDAO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.UnauthorizedException;
import be.vinci.pae.exception.WrongBodyDataException;
import be.vinci.pae.utils.Util;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * UserUCCImpl is a class that implements the UserUCC interface. It provides the implementation for
 * the method to authenticate a user. This class is responsible for handling business logic related
 * to User operations. It interacts with the Data Access Layer (DAL) to retrieve and manipulate User
 * data.
 */
public class UserUCCImpl implements UserUCC {

  // Instance of UserDAO for interacting with the database
  @Inject
  private UserDAO myUserDAO;

  // Instance of DALServices for performing common DAL operations
  @Inject
  private DALServices dalServices;

  /**
   * This method is used to authenticate a user. It first checks if the user exists and if the
   * password is correct. If the checks pass, it logs in the user and returns the user's data.
   *
   * @param email    The email of the user to authenticate.
   * @param password The password of the user to authenticate.
   * @return A UserDTO object if the user is authenticated, null otherwise.
   * @throws UnauthorizedException if the email or password is incorrect.
   */
  public UserDTO login(String email, String password) {
    try {
      // Retrieve the user by email
      dalServices.open();
      User userTemp = (User) myUserDAO.getOneByEmail(email);
      // Check if the user exists and the password is correct
      if (userTemp == null || !userTemp.checkPassword(password)) {
        throw new UnauthorizedException("email or password incorrect");
      }
      // Return the authenticated user
      return userTemp;
    } finally {
      dalServices.close();
    }
  }

  /**
   * This method is used to retrieve a user from the database by their user ID.
   *
   * @param id the id of the user
   * @return A UserDTO object representing the user with the given ID. If not, it returns null.
   * @throws UnauthorizedException if the user is not found.
   */
  @Override
  public UserDTO refreshUser(int id) {
    try {
      dalServices.open();
      UserDTO user = myUserDAO.getOneById(id);
      if (user == null) {
        throw new UnauthorizedException();
      }
      return user;
    } finally {
      dalServices.close();
    }
  }

  /**
   * This method is used to register a new user. It first checks if the user role is not null, then
   * checks if the user already exists in the database. If the user does not exist, it hashes the
   * user password, sets the registration date to the current date, and creates the user in the
   * database. If the user creation is successful, it commits the transaction and returns the user
   * data.
   *
   * @param user The UserDTO object containing the user data.
   * @return The created UserDTO object. If the user creation fails, it throws a RuntimeException.
   * @throws WrongBodyDataException if the user role is null or student and the email ends with
   *                                "@vinci.be".
   * @throws ConflictException      if the user already exists.
   */
  @Override
  public UserDTO register(UserDTO user) {
    // Cast the UserDTO object to User
    User userTemp = (User) user;
    if (!userTemp.checkEmailAndRoleLogic()) {
      throw new WrongBodyDataException("This role is not allowed for this email");
    }
    try {
      // Start a new transaction
      dalServices.startTransaction();
      // Check if the user already exists in the database by email
      if (myUserDAO.getOneByEmail(userTemp.getEmail()) != null) {
        // If the user already exists, rollback the transaction and return null
        throw new ConflictException("this resource already exists");
      }
      // Hash the user password
      userTemp.hashPassword();
      // Set the registration date to the current date
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      userTemp.setRegistrationDate(LocalDate.now().format(formatter));

      userTemp.setSchoolYear(Util.getCurrentSchoolYear());
      // Create the user in the database
      myUserDAO.createOne(userTemp);
      // Commit the transaction
      dalServices.commit();
      // Return the created user
      return userTemp;
    } catch (Exception e) {
      // If an exception occurs, rollback the transaction and throw a RuntimeException
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Retrieves all users from the database. This method opens a connection to the database,
   * retrieves all users, and then closes the connection. It uses the User Data Access Object (DAO)
   * to interact with the database.
   *
   * @return a list of UserDTO objects representing all users in the database
   */
  public List<UserDTO> getAllUsers() {
    try {
      // Open a new session with the database
      dalServices.open();

      // Retrieve all users from the database
      return myUserDAO.getAll();
    } finally {
      // Close the session with the database
      dalServices.close();
    }
  }

  /**
   * This method is used to edit the data of an existing user. It takes a UserDTO object as a
   * parameter, which contains the updated user data. The method first starts a new transaction,
   * then checks if the user already exists in the database by email. If the user does not exist, it
   * rolls back the transaction and throws a ConflictException. If the user exists, it hashes the
   * user password, updates the user data in the database, commits the transaction, and returns the
   * updated UserDTO object.
   *
   * @param user          The UserDTO object containing the updated user data.
   * @param password      The current password of the user.
   * @param newPassword   The new password to be set for the user.
   * @param versionNumber The current version number of the user data.
   * @return The updated UserDTO object. If the user does not exist, it throws a ConflictException.
   * @throws ConflictException      If the user does not exist in the database.
   * @throws WrongBodyDataException If the provided password is incorrect.
   */
  public UserDTO editData(UserDTO user, String password, String newPassword, int versionNumber) {
    User userTemp = (User) user;
    try {
      dalServices.startTransaction();

      if (!userTemp.checkPassword(password)) {
        throw new WrongBodyDataException("password incorrect");
      }
      if (newPassword != null) {
        userTemp.setPassword(newPassword);
        userTemp.hashPassword();
      }

      userTemp.setVersionNumber(versionNumber + 1);
      myUserDAO.updateOne(userTemp);
      dalServices.commit();
      return userTemp;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }


}