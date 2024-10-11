package be.vinci.pae.dal.user;

import be.vinci.pae.business.DomainFactory;
import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.utils.Util;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.FatalException;
import be.vinci.pae.exception.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAOImpl is a class that implements the UserDAO interface. It provides the implementation for
 * the methods to fetch a user by email or by user ID from the database. This implementation
 * utilizes the DomainFactory and DALBackServices to interact with the database.
 */
public class UserDAOImpl implements UserDAO {

  // Instance of DomainFactory
  @Inject
  private DomainFactory myDomainFactory;

  // Instance of DALBackServices
  @Inject
  private DALBackServices dalBackServices;

  /**
   * Fetches a user from the database by their email. This method prepares a SQL statement, executes
   * it, and maps the result to a UserDTO object. If no user is found, it returns null.
   *
   * @param email The email of the user to fetch.
   * @return A UserDTO object if a user is found, null otherwise.
   * @throws RuntimeException if a SQLException occurs.
   */
  @Override
  public UserDTO getOneByEmail(String email) {
    try (PreparedStatement userByEmail =
        dalBackServices.getPreparedStatement(
            "SELECT * FROM pae.users WHERE LOWER(email) = LOWER(?)")) {
      userByEmail.setString(1, email);
      try (var rs = userByEmail.executeQuery()) {
        if (rs.next()) {
          UserDTO user = myDomainFactory.getUser();
          return (UserDTO) Util.fillInfos(rs, user);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  /**
   * Retrieves a user from the database by their user ID. This method prepares a SQL statement,
   * executes it, and maps the result to a UserDTO object. If no user is found, it returns null.
   *
   * @param id The ID of the user to retrieve.
   * @return A UserDTO object representing the user with the given ID. If not, it returns null.
   */
  @Override
  public UserDTO getOneById(int id) {
    try (PreparedStatement userById =
        dalBackServices.getPreparedStatement(
            "SELECT * FROM pae.users WHERE idUser = ?")) {
      userById.setInt(1, id);
      try (var rs = userById.executeQuery()) {
        if (rs.next()) {
          UserDTO user = myDomainFactory.getUser();
          return (UserDTO) Util.fillInfos(rs, user);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  /**
   * Creates a new user in the database. This method prepares a SQL statement, executes it, and maps
   * the result to a UserDTO object. If the user creation fails, it throws a RuntimeException.
   *
   * @param user The UserDTO object containing the user data.
   * @return The created UserDTO object.
   * @throws RuntimeException if a SQLException occurs.
   */
  @Override
  public UserDTO createOne(UserDTO user) {
    try (PreparedStatement userByEmail =
        dalBackServices.getPreparedStatement(
            "INSERT INTO pae.users "
                + "(email, password, lastName, firstName, phoneNumber, "
                + "registrationDate, role, schoolYear) VALUES (?, ?, ?, ?, ?, ?, ?, ?) "
                + "RETURNING idUser"
        )) {
      userByEmail.setString(1, user.getEmail());
      userByEmail.setString(2, user.getPassword());
      userByEmail.setString(3, user.getLastName());
      userByEmail.setString(4, user.getFirstName());
      userByEmail.setString(5, user.getPhoneNumber());
      userByEmail.setString(6, user.getRegistrationDate());
      userByEmail.setString(7, user.getRole().toString());
      userByEmail.setString(8, user.getSchoolYear());
      try (var rs = userByEmail.executeQuery()) {
        if (rs.next()) {
          user.setIdUser(rs.getInt(1));
          return user;
        }
        return null;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves all users from the database.
   *
   * @return A list of UserDTO objects representing all users.
   * @throws FatalException if a database access error occurs.
   */
  public List<UserDTO> getAll() {
    List<UserDTO> liste = new ArrayList<>();
    try (PreparedStatement users =
        dalBackServices.getPreparedStatement(
            "SELECT * FROM pae.users ")) {
      try (var rs = users.executeQuery()) {
        while (rs.next()) {
          UserDTO user = myDomainFactory.getUser();
          liste.add((UserDTO) Util.fillInfos(rs, user));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return liste;
  }

  /**
   * This method is used to update the details of an existing user in the database. It takes a
   * UserDTO object as a parameter, which contains the updated user data. The method updates the
   * user data in the database based on the provided UserDTO object and returns a UserDTO object
   * representing the updated user. If the update operation fails, it throws a FatalException.
   *
   * @param user The UserDTO object containing the updated user data.
   * @return The updated UserDTO object. If the update operation fails, it throws a FatalException.
   * @throws FatalException If a SQLException occurs during the update operation.
   */
  public UserDTO updateOne(UserDTO user) {
    try (PreparedStatement ps =
        dalBackServices.getPreparedStatement(
            "UPDATE pae.users "
                + "SET email = ?, password = ?, phoneNumber = ?, lastName = ?, firstName = ?, "
                + "registrationDate = ?, role = ?, schoolYear = ?, versionNumber = ? "
                + "WHERE idUser = ? AND versionNumber = ?"
        )) {
      ps.setString(1, user.getEmail());
      ps.setString(2, user.getPassword());
      ps.setString(3, user.getPhoneNumber());
      ps.setString(4, user.getLastName());
      ps.setString(5, user.getFirstName());
      ps.setString(6, user.getRegistrationDate());
      ps.setString(7, user.getRole().toString());
      ps.setString(8, user.getSchoolYear());
      ps.setInt(9, user.getVersionNumber());
      ps.setInt(10, user.getIdUser());
      ps.setInt(11, user.getVersionNumber() - 1);
      boolean result = ps.executeUpdate() == 1;
      if (!result) {
        if (getOneById(user.getIdUser()) == null) {
          throw new ObjectNotFoundException("User not found");
        } else {
          throw new ConflictException("Version number mismatch");
        }
      }
      return user;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

}
