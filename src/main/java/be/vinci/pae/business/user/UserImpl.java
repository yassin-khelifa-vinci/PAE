package be.vinci.pae.business.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mindrot.jbcrypt.BCrypt;

/**
 * UserImpl is a class that implements the User interface. It provides the implementation for the
 * User methods. The @JsonIgnore annotation is used to avoid sending properties not linked to a JSON
 * view. This class represents a User entity with properties such as id, email, password, lastName,
 * firstName, phoneNumber, registrationDate, role, and token.
 */
public class UserImpl implements User {

  // The id of the user, visible in public views
  private int idUser;

  // The login of the user, visible in public views
  private String email;

  // The password of the user, visible in internal views
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  // The name of the user
  private String lastName;

  // The first name of the user
  private String firstName;

  // The phone number of the user
  private String phoneNumber;

  // The date of registration of the user
  private String registrationDate;

  // The role of the user
  private Role role;

  private String schoolYear;

  private int versionNumber;

  /**
   * Returns the login of the user.
   *
   * @return the login of the user
   */
  @Override
  public String getEmail() {
    return email;
  }

  /**
   * Sets the login of the user.
   *
   * @param email the new login of the user
   */
  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the id of the user.
   *
   * @return the id of the user
   */
  @Override
  public int getIdUser() {
    return idUser;
  }

  /**
   * Sets the id of the user.
   *
   * @param idUser the new id of the user
   */
  @Override
  public void setIdUser(int idUser) {
    this.idUser = idUser;
  }

  /**
   * Returns the password of the user.
   *
   * @return the password of the user
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password of the user.
   *
   * @param password the new password of the user
   */
  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the name of the user.
   *
   * @return the name of the user
   */
  @Override
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the name of the user.
   *
   * @param lastName the new name of the user
   */
  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns the first name of the user.
   *
   * @return the first name of the user
   */
  @Override
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of the user.
   *
   * @param firstName the new first name of the user
   */
  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Returns the phone number of the user.
   *
   * @return the phone number of the user
   */
  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Sets the phone number of the user.
   *
   * @param phoneNumber the new phone number of the user
   */
  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Returns the date of registration of the user.
   *
   * @return the date of registration of the user
   */
  @Override
  public String getRegistrationDate() {
    return registrationDate;
  }

  /**
   * Sets the date of registration of the user.
   *
   * @param registrationDate the new date of registration of the user
   */
  @Override
  public void setRegistrationDate(String registrationDate) {
    this.registrationDate = registrationDate;
  }

  /**
   * Returns the role of the user.
   *
   * @return the role of the user
   */
  @Override
  public Role getRole() {
    return role;
  }

  /**
   * Sets the role of the user.
   *
   * @param role the new role of the user
   */
  @Override
  public void setRole(String role) {
    this.role = Role.valueOf(role);
  }

  @Override
  public String getSchoolYear() {
    return schoolYear;
  }

  @Override
  public void setSchoolYear(String schoolYear) {
    this.schoolYear = schoolYear;
  }

  /**
   * Get version number method. This method returns the version number of the user.
   *
   * @return int The version number of the user.
   */
  @Override
  public int getVersionNumber() {
    return versionNumber;
  }

  /**
   * Set version number method. This method sets the version number of the user.
   *
   * @param versionNumber The new version number of the user.
   */
  @Override
  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
  }

  /**
   * Checks if the provided password matches the user's password. It uses the BCrypt.checkpw method
   * to compare the provided password with the user's hashed password.
   *
   * @param password the password to check
   * @return true if the provided password matches the user's password, false otherwise
   */
  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  /**
   * Hashes the user's password using the BCrypt.hashpw method. The hashed password is then stored
   * in the password property of the User object.
   */
  @Override
  public void hashPassword() {
    this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());
  }

  /**
   * Checks if the email and role of the user are logically consistent. If the email ends with
   * "@vinci.be", the role cannot be STUDENT. If the email ends with "@student.vinci.be", the role
   * must be STUDENT.
   *
   * @return true if the email and role are logically consistent, false otherwise
   */
  @Override
  public boolean checkEmailAndRoleLogic() {
    if (this.email.endsWith("@vinci.be") && this.role.equals(Role.STUDENT)) {
      return false;
    }
    return !this.email.endsWith("@student.vinci.be") || this.role.equals(Role.STUDENT);
  }
}