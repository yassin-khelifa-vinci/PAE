package be.vinci.pae.business.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * UserDTO interface. This interface represents a User in the system. It provides methods for
 * getting and setting the user's login, id, password, and token. It also provides methods for
 * checking and hashing the password. The @JsonDeserialize annotation specifies that instances of
 * this type should be deserialized using the UserImpl class.
 */
@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  /**
   * Get login method. This method returns the login of the user.
   *
   * @return String The login of the user.
   */
  String getEmail();

  /**
   * Set login method. This method sets the login of the user.
   *
   * @param email The new login of the user.
   */
  void setEmail(String email);

  /**
   * Get id method. This method returns the id of the user.
   *
   * @return int The id of the user.
   */
  int getIdUser();

  /**
   * Set id method. This method sets the id of the user.
   *
   * @param idUser The new id of the user.
   */
  void setIdUser(int idUser);

  /**
   * Get password method. This method returns the password of the user.
   *
   * @return String The password of the user.
   */
  String getPassword();

  /**
   * Set password method. This method sets the password of the user.
   *
   * @param password The new password of the user.
   */
  void setPassword(String password);

  /**
   * Get name method. This method returns the name of the user.
   *
   * @return String The name of the user.
   */
  String getLastName();

  /**
   * Set name method. This method sets the name of the user.
   *
   * @param lastName The new name of the user.
   */
  void setLastName(String lastName);

  /**
   * Get first name method. This method returns the first name of the user.
   *
   * @return String The first name of the user.
   */
  String getFirstName();

  /**
   * Set first name method. This method sets the first name of the user.
   *
   * @param firstName The new first name of the user.
   */
  void setFirstName(String firstName);

  /**
   * Get phone number method. This method returns the phone number of the user.
   *
   * @return String The phone number of the user.
   */
  String getPhoneNumber();

  /**
   * Set phone number method. This method sets the phone number of the user.
   *
   * @param phoneNumber The new phone number of the user.
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Get date of registration method. This method returns the date of registration of the user.
   *
   * @return String The date of registration of the user.
   */
  String getRegistrationDate();

  /**
   * Set date of registration method. This method sets the date of registration of the user.
   *
   * @param registrationDate The new date of registration of the user.
   */
  void setRegistrationDate(String registrationDate);

  /**
   * Get school year method. This method returns the school year of the user.
   *
   * @return String The school year of the user.
   */
  String getSchoolYear();

  /**
   * Set school year method. This method sets the school year of the user.
   *
   * @param schoolYear The new school year of the user.
   */
  void setSchoolYear(String schoolYear);

  /**
   * Get role method. This method returns the role of the user.
   *
   * @return Role The role of the user.
   */
  Role getRole();

  /**
   * Set role method. This method sets the role of the user.
   *
   * @param role The new role of the user.
   */
  void setRole(String role);

  /**
   * Get version number method. This method returns the version number of the user.
   *
   * @return int The version number of the user.
   */
  int getVersionNumber();

  /**
   * Set version number method. This method sets the version number of the user.
   *
   * @param versionNumber The new version number of the user.
   */
  void setVersionNumber(int versionNumber);

  /**
   * Role enumeration. This enumeration represents the possible roles a user can have in the system.
   * It also provides a method to find a role based on a string representation.
   */
  enum Role {

    /**
     * STUDENT role represents a student user.
     */
    STUDENT,

    /**
     * TEACHER role represents a teacher user.
     */
    TEACHER,

    /**
     * ADMINISTRATIVE role represents an administrative user.
     */
    ADMINISTRATIVE;

  }
}