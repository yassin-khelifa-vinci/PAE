package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.contact.ContactUCC;
import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.enterprise.EnterpriseDAO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.FatalException;
import be.vinci.pae.exception.ObjectNotFoundException;
import be.vinci.pae.exception.WrongBodyDataException;
import be.vinci.pae.utils.ApplicationBinderTest;
import be.vinci.pae.utils.Util;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class for ContactUCCImpl.
 */
public class ContactUCCImplTest {

  // ServiceLocator instance for testing
  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
  // Instance of ContactUCC for testing
  private final ContactUCC contactUCC = locator.getService(ContactUCC.class);
  // Mocked ContactDAO for testing
  private final ContactDAO myContactDAO = locator.getService(ContactDAO.class);
  // Instance of DomainFactory
  private final DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private final EnterpriseDAO enterpriseDAO = locator.getService(EnterpriseDAO.class);
  // ContactDTO instance for testing
  private ContactDTO contact;
  private EnterpriseDTO enterprise;

  /**
   * This method sets up the necessary objects for testing. It is annotated with @BeforeEach to run
   * before each test case.
   */
  @BeforeEach
  void setUp() {

    // Initialization of a stage
    this.contact = domainFactory.getContact();

    this.enterprise = domainFactory.getEnterprise();
  }

  /**
   * This test case checks the getMyContacts functionality. It expects a list of contacts.
   */
  @Test
  @DisplayName("Test getMyContacts method")
  void getContact() {
    Mockito.when(myContactDAO.getContactsByUser(1)).thenReturn(List.of(contact));

    List<ContactDTO> result = contactUCC.getMyContacts(1);
    assertEquals(List.of(contact), result);
  }

  /**
   * Test case to verify the behavior of createContact method when attempting to create a contact
   * with a non-existent enterprise.
   */
  @Test
  @DisplayName("Test createContact non existent enterprise")
  void createContactFalseEnterprise() {
    Mockito.when(enterpriseDAO.getOneById(1)).thenReturn(null);
    assertThrows(ObjectNotFoundException.class,
        () -> contactUCC.createContact(1, 1));
  }

  @Test
  @DisplayName("Test createContact but user has a contact accepted")
  void createContactAccepted() {
    Mockito.when(enterpriseDAO.getOneById(1)).thenReturn(enterprise);
    Mockito.when(myContactDAO.getContactAccepted(1)).thenReturn(contact);
    assertThrows(ConflictException.class,
        () -> contactUCC.createContact(1, 1));

  }

  /**
   * Test case to verify the behavior of createContact method when attempting to create a contact
   * that already exists.
   */
  @Test
  @DisplayName("Test createContact already existent contact")
  void createContactExistentContact() {
    Mockito.when(enterpriseDAO.getOneById(1)).thenReturn(enterprise);
    Mockito.when(myContactDAO.getContactAccepted(1)).thenReturn(null);
    Mockito.when(myContactDAO
            .getContactByUserAndByEnterprise(1, 1, Util.getCurrentSchoolYear()))
        .thenReturn(contact);

    assertThrows(ConflictException.class,
        () -> contactUCC.createContact(1, 1));
  }

  /**
   * Test case to verify the behavior of createContact method when the creation process fails.
   */
  @Test
  @DisplayName("Test createContact create fail")
  void createContactFail() {
    Mockito.when(myContactDAO.createOne(Mockito.any(ContactDTO.class)))
        .thenReturn(null);
    Mockito.when(enterpriseDAO.getOneById(1)).thenReturn(enterprise);
    Mockito.when(myContactDAO.getContactAccepted(1)).thenReturn(null);
    Mockito.when(myContactDAO
            .getContactByUserAndByEnterprise(1, 1, Util.getCurrentSchoolYear()))
        .thenReturn(null);

    assertThrows(FatalException.class,
        () -> contactUCC.createContact(1, 1));
  }

  /**
   * Test case to verify the successful creation of a contact when all parameters are correct.
   */
  @Test
  @DisplayName("Test createContact all ok")
  void createContactOk() {
    Mockito.when(myContactDAO.createOne(Mockito.any(ContactDTO.class)))
        .thenReturn(contact);
    Mockito.when(enterpriseDAO.getOneById(1)).thenReturn(enterprise);
    Mockito.when(myContactDAO.getContactAccepted(1)).thenReturn(null);
    Mockito.when(myContactDAO
            .getContactByUserAndByEnterprise(1, 1, Util.getCurrentSchoolYear()))
        .thenReturn(null);

    assertEquals(contact.getIdContact(), contactUCC.createContact(1, 1).getIdContact());
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contact is not found.
   */
  @Test
  @DisplayName("Test update contactStatus invalid contact")
  void updateContactStatusInvalidContact() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(id);
    contact.setUserId(idUser);
    contact.setVersionNumber(versionNumber);
    contact.setContactStatus("ADMITTED");
    Mockito.when(myContactDAO.getContact(3)).thenReturn(null);
    assertThrows(ObjectNotFoundException.class,
        () -> contactUCC.acceptContact(3, idUser, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus invalid user")
  void updateContactStatusInvalidUser() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(idUser);
    contact.setContactStatus("ADMITTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    assertThrows(WrongBodyDataException.class,
        () -> contactUCC.acceptContact(id, 3, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus invalid status")
  void changeStatusAcceptedInvalidStatus() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("STARTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    assertThrows(WrongBodyDataException.class,
        () -> contactUCC.acceptContact(id, idUser, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus invalid status")
  void changeStatusUnsupervisedInvalidStatus() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("ACCEPTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    assertThrows(WrongBodyDataException.class,
        () -> contactUCC.unsupervisedContact(id, idUser, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus invalid status")
  void changeStatusAdmittedInvalidStatus() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("ACCEPTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    assertThrows(WrongBodyDataException.class,
        () -> contactUCC.admitContact(id, idUser, "place", versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus invalid status")
  void changeStatusTurnedDownInvalidStatus() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("STARTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    assertThrows(WrongBodyDataException.class,
        () -> contactUCC.turnDownContact(id, idUser, "reason", versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the version number is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus invalid version number")
  void updateContactStatusInvalidNumber() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("ADMITTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    assertThrows(FatalException.class,
        () -> contactUCC.acceptContact(id, idUser, 4));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus result false")
  void updateContactStatusFalse() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("ADMITTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    Mockito.when(myContactDAO.changeStatus(contact, versionNumber)).thenReturn(false);
    assertThrows(FatalException.class,
        () -> contactUCC.acceptContact(id, idUser, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus result false")
  void updateContactStatusFalseTurnedDown() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    final String reasonRefusal = "I don't want";
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("ADMITTED");
    contact.setVersionNumber(versionNumber);
    contact.setReasonForRefusal(reasonRefusal);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    Mockito.when(myContactDAO.changeStatus(contact, versionNumber)).thenReturn(false);
    assertThrows(FatalException.class,
        () -> contactUCC.turnDownContact(id, idUser, reasonRefusal, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */

  @Test
  @DisplayName("Test update contactStatus result false")
  void updateContactStatusFalseTurnedDownAdmitted() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    final String meetringPlace = "I don't want";
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("STARTED");
    contact.setVersionNumber(versionNumber);
    contact.setMeetingPlace(meetringPlace);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    Mockito.when(myContactDAO.changeStatus(contact, versionNumber)).thenReturn(false);
    assertThrows(FatalException.class,
        () -> contactUCC.admitContact(id, idUser, meetringPlace, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus result false")
  void updateContactStatusFalseUnsupervised() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("ADMITTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    Mockito.when(myContactDAO.changeStatus(contact, versionNumber)).thenReturn(false);
    assertThrows(FatalException.class,
        () -> contactUCC.unsupervisedContact(id, idUser, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus accept result true")
  void updateContactStatusAcceptTrue() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("ADMITTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    Mockito.when(myContactDAO.changeStatus(contact, versionNumber)).thenReturn(true);
    assertEquals(contact, contactUCC.acceptContact(id, idUser, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus turn down result true")
  void updateContactStatusTurnDownTrue() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("ADMITTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    Mockito.when(myContactDAO.changeStatus(contact, versionNumber)).thenReturn(true);
    assertEquals(contact, contactUCC.turnDownContact(id, idUser, "reason", versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus unsupervised result true")
  void updateContactStatusUnsupervisedTrue() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("ADMITTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    Mockito.when(myContactDAO.changeStatus(contact, versionNumber)).thenReturn(true);
    assertEquals(contact, contactUCC.unsupervisedContact(id, idUser, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus unsupervised result true")
  void updateContactStatusUnsupervisedTrue2() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("STARTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    Mockito.when(myContactDAO.changeStatus(contact, versionNumber)).thenReturn(true);
    assertEquals(contact, contactUCC.unsupervisedContact(id, idUser, versionNumber));
  }

  /**
   * Test case to verify the behavior of update contactStatus method when the contactStatus is
   * invalid.
   */
  @Test
  @DisplayName("Test update contactStatus admit result true")
  void updateContactStatusAdmitTrue() {
    final int id = 1;
    final int idUser = 2;
    final int versionNumber = 1;
    contact.setIdContact(1);
    contact.setUserId(2);
    contact.setContactStatus("STARTED");
    contact.setVersionNumber(versionNumber);
    Mockito.when(myContactDAO.getContact(1)).thenReturn(contact);
    Mockito.when(myContactDAO.changeStatus(contact, versionNumber)).thenReturn(true);
    assertEquals(contact, contactUCC.admitContact(id, idUser, "place", versionNumber));
  }


  /**
   * Test case to verify the behavior of getEnterpriseContact method.
   */
  @Test
  @DisplayName("Test getEnterpriseContact method")
  void getEnterpriseContact() {
    Mockito.when(myContactDAO.getContactsByEnterprise(1)).thenReturn(List.of(contact));
    List<ContactDTO> result = contactUCC.getEnterpriseContacts(1);
    assertEquals(List.of(contact), result);
  }

  /**
   * Test case to verify the behavior of getUserContact method.
   */
  @Test
  @DisplayName("Test getUserContacts method")
  void getUserContacts() {
    List<ContactDTO> contacts = List.of(contact);
    Mockito.when(myContactDAO.getContactsByUser(1)).thenReturn(contacts);
    assertEquals(contacts, contactUCC.getUserContacts(1));
  }
}