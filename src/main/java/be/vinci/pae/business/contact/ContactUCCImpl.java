package be.vinci.pae.business.contact;

import be.vinci.pae.business.DomainFactory;
import be.vinci.pae.business.contact.ContactDTO.Status;
import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.enterprise.EnterpriseDAO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.FatalException;
import be.vinci.pae.exception.ObjectNotFoundException;
import be.vinci.pae.exception.UnauthorizedException;
import be.vinci.pae.exception.WrongBodyDataException;
import be.vinci.pae.utils.Util;
import jakarta.inject.Inject;
import java.util.List;

/**
 * This class provides the business logic for the authentication process.
 */
public class ContactUCCImpl implements ContactUCC {

  // Instance of AuthsDAO for interacting with the database
  @Inject
  private ContactDAO myContactDAO;

  @Inject
  private EnterpriseDAO myEnterpriseDAO;

  // Instance of DALServices for performing common DAL operations
  @Inject
  private DALServices dalServices;

  // Instance of DomainFactory for creating domain objects
  @Inject
  private DomainFactory domainFactory;

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id The unique identifier of the request.
   * @return The contacts related to the specified request.
   */
  @Override
  public List<ContactDTO> getMyContacts(int id) {
    try {
      dalServices.open();
      return myContactDAO.getContactsByUser(id);
    } finally {
      dalServices.close();
    }
  }

  /**
   * Creates a new contact for the specified user and enterprise IDs.
   *
   * @param idUser       The ID of the user associated with the contact.
   * @param idEnterprise The ID of the enterprise associated with the contact.
   * @return The ContactDTO object representing the newly created contact.
   */
  @Override
  public ContactDTO createContact(int idUser, int idEnterprise) {
    try {
      dalServices.startTransaction();
      EnterpriseDTO enterpriseTemp = myEnterpriseDAO.getOneById(idEnterprise);
      if (enterpriseTemp == null) {
        throw new ObjectNotFoundException("Enterprise doesn't exist");
      }
      if (myContactDAO.getContactAccepted(idUser) != null) {
        throw new ConflictException("Accepted contact for this user exist");
      }
      ContactDTO contact = domainFactory.getContact();
      contact.setUserId(idUser);
      contact.setEnterprise(idEnterprise);
      contact.setContactStatus(Status.STARTED.toString());
      contact.setSchoolYear(Util.getCurrentSchoolYear());
      contact.setVersionNumber(1);

      if (myContactDAO.getContactByUserAndByEnterprise(idUser, idEnterprise,
          contact.getSchoolYear())
          != null) {
        throw new ConflictException("Contact already exists");
      }

      if (myContactDAO.createOne(contact) == null) {
        throw new FatalException("New Contact creation failed");
      }
      dalServices.commit();
      return contact;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id            The unique identifier of the request.
   * @param idUser        The unique identifier of the user.
   * @param versionNumber The version number of the contact.
   * @return The contacts related to the specified request.
   */
  @Override
  public ContactDTO acceptContact(int id, int idUser, int versionNumber) {
    try {
      dalServices.startTransaction();
      Contact contactTemp = (Contact) myContactDAO.getContact(id);

      checkContact(contactTemp, Status.ACCEPTED, idUser, versionNumber);

      boolean result = myContactDAO.changeStatus(contactTemp, versionNumber);
      if (!result) {
        throw new FatalException("Status change failed");
      }

      myContactDAO.changeAllStatus(contactTemp);

      dalServices.commit();
      return contactTemp;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id            The unique identifier of the request.
   * @param idUser        The unique identifier of the user.
   * @param reasonRefusal The reason of refusal.
   * @param versionNumber The version number of the contact.
   * @return The contacts related to the specified request.
   */
  @Override
  public ContactDTO turnDownContact(int id, int idUser, String reasonRefusal, int versionNumber) {
    try {
      dalServices.startTransaction();
      Contact contactTemp = (Contact) myContactDAO.getContact(id);

      checkContact(contactTemp, Status.TURNED_DOWN, idUser, versionNumber);

      contactTemp.setReasonForRefusal(reasonRefusal);
      boolean result = myContactDAO.changeStatus(contactTemp, versionNumber);
      if (!result) {
        throw new FatalException("Status change failed");
      }

      dalServices.commit();
      return contactTemp;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id            The unique identifier of the request.
   * @param idUser        The unique identifier of the user.
   * @param meetingPlace  The place of the meeting.
   * @param versionNumber The version number of the contact.
   * @return The contacts related to the specified request.
   */
  @Override
  public ContactDTO admitContact(int id, int idUser, String meetingPlace, int versionNumber) {
    try {
      dalServices.startTransaction();
      Contact contactTemp = (Contact) myContactDAO.getContact(id);

      checkContact(contactTemp, Status.ADMITTED, idUser, versionNumber);

      contactTemp.setMeetingPlace(meetingPlace);
      boolean result = myContactDAO.changeStatus(contactTemp, versionNumber);
      if (!result) {
        throw new FatalException("Status change failed");
      }

      dalServices.commit();
      return contactTemp;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id            The unique identifier of the request.
   * @param idUser        The unique identifier of the user.
   * @param versionNumber The version number of the contact.
   * @return The contacts related to the specified request.
   */
  @Override
  public ContactDTO unsupervisedContact(int id, int idUser, int versionNumber) {
    try {
      dalServices.startTransaction();
      Contact contactTemp = (Contact) myContactDAO.getContact(id);

      checkContact(contactTemp, Status.UNSUPERVISED, idUser, versionNumber);

      boolean result = myContactDAO.changeStatus(contactTemp, versionNumber);
      if (!result) {
        throw new FatalException("Status change failed");
      }

      dalServices.commit();
      return contactTemp;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id The unique identifier of the request.
   * @return The contacts related to the specified request.
   */
  @Override
  public List<ContactDTO> getEnterpriseContacts(int id) {
    try {
      dalServices.open();
      return myContactDAO.getContactsByEnterprise(id);
    } finally {
      dalServices.close();
    }
  }

  /**
   * Retrieves the contacts associated with a specific user. This method opens a connection to the
   * data access layer, fetches all contacts associated with the given user ID, and then closes the
   * connection. If no contacts are found, it throws an UnauthorizedException.
   *
   * @param id The unique identifier of the user.
   * @return A list of ContactDTO objects representing the contacts of the specified user.
   * @throws UnauthorizedException if no contacts are found for the specified user.
   */
  @Override
  public List<ContactDTO> getUserContacts(int id) {
    try {
      dalServices.open();
      return myContactDAO.getContactsByUser(id);
    } finally {
      dalServices.close();
    }
  }

  /**
   * Change the status of a contact.
   *
   * @param contact       of the request
   * @param status        of the request
   * @param idUser        of the request
   * @param versionNumber of the request
   */
  private void checkContact(Contact contact, Status status, int idUser, int versionNumber) {
    if (contact == null) {
      throw new ObjectNotFoundException("Contact not found");
    }

    if (!contact.verification(status, idUser)) {
      throw new WrongBodyDataException("Status change not allowed");
    }

    contact.setContactStatus(status.toString());
    contact.setVersionNumber(versionNumber + 1);

  }


}
