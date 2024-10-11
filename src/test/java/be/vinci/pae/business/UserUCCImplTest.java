package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.business.user.UserUCC;
import be.vinci.pae.dal.user.UserDAO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.UnauthorizedException;
import be.vinci.pae.exception.WrongBodyDataException;
import be.vinci.pae.utils.ApplicationBinderTest;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * This class is a test class for UserUCCImpl. It tests the methods of UserUCCImpl class. It uses
 * JUnit and Mockito for testing.
 */
class UserUCCImplTest {

  // ServiceLocator instance for testing
  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());

  // Instance of UserUCC for testing
  private final UserUCC userUCC = locator.getService(UserUCC.class);

  // Mocked UserDAO for testing
  private final UserDAO myUserDAO = locator.getService(UserDAO.class);

  // Instance of DomainFactory
  private final DomainFactory domainFactory = locator.getService(DomainFactory.class);

  // UserDTO instance for testing
  private UserDTO user;

  /**
   * This method sets up the necessary objects for testing. It is annotated with @BeforeEach to run
   * before each test case.
   */
  @BeforeEach
  void setUp() {
    // Initialization of a user
    this.user = domainFactory.getUser();
    this.user.setEmail("good.email@vinci.be");
    // hashed value of "password"
    this.user.setPassword(
        "$2a$10$CIdqI8rNAs15op/5PUID0e8H2A6mQ7so3RzEtKx3tdk1jsJ2I9JaW");

    // Mocking the behavior of myUserDAO
    Mockito.when(myUserDAO.getOneByEmail("good.email@vinci.be")).thenReturn(user);
  }

  /**
   * This test case checks the login functionality with a wrong email. It expects a null return
   * value.
   */
  @DisplayName("wrong email for user login")
  @Test
  void falseEmail() {
    Mockito.when(myUserDAO.getOneByEmail("wrong.email@vinci.be")).thenReturn(null);

    assertThrows(UnauthorizedException.class,
        () -> userUCC.login("wrong.email@vinci.be", "password"));
  }

  /**
   * This test case checks the login functionality with a wrong password. It expects a null return
   * value.
   */
  @DisplayName("wrong password for user login")
  @Test
  void falsePassword() {
    assertThrows(UnauthorizedException.class,
        () -> userUCC.login("good.email@vinci.be", "wrongpassword"));
  }

  /**
   * This test case checks the login functionality with correct credentials. It expects the user
   * object as the return value.
   */
  @DisplayName("True user login")
  @Test
  void trueLogin() {
    assertEquals(user, userUCC.login("good.email@vinci.be", "password"));
  }

  /**
   * This test case checks the refreshUser functionality. It expects the user object as the return
   */
  @DisplayName("Refresh user")
  @Test
  void refreshUser() {
    Mockito.when(myUserDAO.getOneById(1)).thenReturn(user);

    assertEquals(user, userUCC.refreshUser(1));
  }

  /**
   * Test case for refreshing a user with an invalid ID. This test case expects an
   * UnauthorizedException to be thrown when trying to refresh a user with an ID of 0.
   */
  @DisplayName("Refresh user with wrong id")
  @Test
  void refreshUser1() {
    assertThrows(UnauthorizedException.class, () -> userUCC.refreshUser(0));
  }

  /**
   * Test case for registering a user with a student email and a teacher role. This test case
   * expects a WrongBodyDataException to be thrown when trying to register a user with a student
   * email and a teacher role.
   */
  @DisplayName("Register user with student email and teacher role ")
  @Test
  void register1() {
    user.setEmail("test.test@student.vinci.be");
    user.setRole("TEACHER");

    assertThrows(WrongBodyDataException.class, () -> userUCC.register(user));
  }

  /**
   * Test case for registering a user with a student email and an administrative role. This test
   * case expects a WrongBodyDataException to be thrown when trying to register a user with a
   * student email and an administrative role.
   */
  @DisplayName("Register user with student email and administrative role ")
  @Test
  void register2() {
    user.setEmail("test.test@student.vinci.be");
    user.setRole("ADMINISTRATIVE");

    assertThrows(WrongBodyDataException.class, () -> userUCC.register(user));
  }

  /**
   * Test case for registering a user with a teacher/administrative email and a student role. This
   * test case expects a WrongBodyDataException to be thrown when trying to register a user with a
   * teacher/administrative email and a student role.
   */
  @DisplayName("Register user with teacher/administrative email and student role ")
  @Test
  void register3() {
    user.setEmail("test.test@vinci.be");
    user.setRole("STUDENT");

    assertThrows(WrongBodyDataException.class, () -> userUCC.register(user));
  }

  /**
   * Test case for registering a user with a teacher/administrative email and a teacher role. This
   * test case expects the registered user to be returned and checks that the password has been
   * hashed and that the registration date and school year are not null.
   */
  @DisplayName("Register user with teacher/administrative email and teacher role ")
  @Test
  void register4() {
    user.setEmail("test.test@vinci.be");
    user.setRole("TEACHER");
    user.setPassword("password");

    assertEquals(user, userUCC.register(user));
    assertNotNull(user.getRegistrationDate());
    assertNotNull(user.getSchoolYear());
    assertNotEquals("password", user.getPassword());
  }

  /**
   * Test case for registering a user with a teacher/administrative email and an administrative
   * role. This test case expects the registered user to be returned and checks that the password
   * has been hashed and that the registration date and school year are not null.
   */
  @DisplayName("Register user with teacher/administrative email and administrative role ")
  @Test
  void register5() {
    user.setEmail("test.test@vinci.be");
    user.setRole("ADMINISTRATIVE");
    user.setPassword("password");

    assertEquals(user, userUCC.register(user));
    assertNotNull(user.getRegistrationDate());
    assertNotNull(user.getSchoolYear());
    assertNotEquals("password", user.getPassword());
  }

  /**
   * Test case for registering a user with a student email and a student role. This test case
   * expects the registered user to be returned and checks that the password has been hashed and
   * that the registration date and school year are not null.
   */
  @DisplayName("Register user with student email and student role ")
  @Test
  void register6() {
    user.setEmail("test.test@student.vinci.be");
    user.setRole("STUDENT");
    user.setPassword("password");

    assertEquals(user, userUCC.register(user));
    assertNotNull(user.getRegistrationDate());
    assertNotNull(user.getSchoolYear());
    assertNotEquals("password", user.getPassword());
  }

  /**
   * Test case for registering a user with an email that already exists. This test case expects a
   * ConflictException to be thrown when trying to register a user with an email that already
   * exists.
   */
  @DisplayName("Register user with email that already exists")
  @Test
  void register7() {
    user.setEmail("test.test@vinci.be");
    user.setRole("TEACHER");

    Mockito.when(myUserDAO.getOneByEmail("test.test@vinci.be")).thenReturn(user);

    assertThrows(ConflictException.class, () -> userUCC.register(user));
  }

  /**
   * Test case for getting all users. This test case expects a list of all users to be returned.
   */
  @DisplayName("Get all users")
  @Test
  void getAllUsers() {
    List<UserDTO> users = new ArrayList<>();
    users.add(user);

    Mockito.when(myUserDAO.getAll()).thenReturn(users);

    assertEquals(users, userUCC.getAllUsers());
  }

  /**
   * This test case checks the editData method of the UserUCC class when no new password is
   * provided. It first asserts that the returned user from the editData method is the same as the
   * user used for testing. Then it checks if the version number of the user has been incremented to
   * 2 after the edit.
   */
  @DisplayName("test edit data method without new password")
  @Test
  void testEditData() {
    assertEquals(user, userUCC.editData(user, "password", null,
        1));
    assertEquals(2, user.getVersionNumber());
  }

  /**
   * This test case checks the editData method of the UserUCC class when a new password is provided.
   * It first asserts that the returned user from the editData method is the same as the user used
   * for testing. Then it checks if the version number of the user has been incremented to 2 after
   * the edit. Finally, it asserts that the returned user from the editData method is the same as
   * the user used for testing when the password is changed again.
   */
  @DisplayName("test edit data method with new password")
  @Test
  void testEditData2() {
    assertEquals(user, userUCC.editData(user, "password", "newPassword",
        1));
    assertEquals(2, user.getVersionNumber());
    // Check if the password has been changed
    assertEquals(user, userUCC.editData(user, "newPassword", null,
        2));
  }

  /**
   * This test case checks the editData method of the UserUCC class when a wrong password is
   * provided. It expects a WrongBodyDataException to be thrown when trying to edit the user data
   * with a wrong password.
   */
  @DisplayName("test edit data method with wrong password")
  @Test
  void testEditData3() {
    assertThrows(WrongBodyDataException.class,
        () -> userUCC.editData(user, "wrongPassword", null, 1));
  }
}