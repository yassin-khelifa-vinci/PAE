package be.vinci.pae.business.user;

/**
 * User interface. This interface extends the UserDTO interface and provides additional methods for
 * handling user passwords. It is part of the business logic layer of the application and is used to
 * define the operations that can be performed on a User object.
 */
public interface User extends UserDTO {

  /**
   * Checks if the provided password matches the user's password. This method is used during user
   * authentication. It takes a string representing the password to check and returns a boolean
   * indicating whether the provided password matches the user's password.
   *
   * @param password the password to check
   * @return true if the provided password matches the user's password, false otherwise
   */
  boolean checkPassword(String password);

  /**
   * Hashes the user's password. This method is used during user registration and password update
   * operations. It takes no parameters and does not return a value. After this method is called,
   * the user's password will be hashed using a secure hashing algorithm.
   */
  void hashPassword();

  /**
   * Checks if the email and role of the user are logically consistent. If the email ends with
   * "@vinci.be", the role cannot be STUDENT. If the email ends with "@student.vinci.be", the role
   * must be STUDENT.
   *
   * @return true if the email and role are logically consistent, false otherwise
   */
  boolean checkEmailAndRoleLogic();
}