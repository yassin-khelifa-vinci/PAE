package be.vinci.pae.business.contact;

import java.util.List;

/**
 * This interface provides the methods for the business logic of the authentication process.
 */
public interface ContactUCC {

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id The unique identifier of the request.
   * @return The contacts related to the specified request.
   */
  List<ContactDTO> getMyContacts(int id);

  /**
   * Creates a new contact for the specified user and enterprise IDs.
   *
   * @param idUser The ID of the user associated with the contact.
   * @param id     The ID of the enterprise associated with the contact.
   * @return The ContactDTO object representing the newly created contact.
   */
  ContactDTO createContact(int idUser, int id);

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id            The unique identifier of the request.
   * @param idUser        The unique identifier of the user.
   * @param versionNumber The version number of the request.
   * @return The contacts related to the specified request.
   */
  ContactDTO acceptContact(int id, int idUser, int versionNumber);

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id            The unique identifier of the request.
   * @param idUser        The unique identifier of the user.
   * @param reasonRefusal The reason of refusal.
   * @param versionNumber The version number of the request.
   * @return The contacts related to the specified request.
   */
  ContactDTO turnDownContact(int id, int idUser, String reasonRefusal, int versionNumber);

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id            The unique identifier of the request.
   * @param idUser        The unique identifier of the user.
   * @param meetingPlace  The place of the meeting.
   * @param versionNumber The version number of the request.
   * @return The contacts related to the specified request.
   */
  ContactDTO admitContact(int id, int idUser, String meetingPlace, int versionNumber);

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id            The unique identifier of the request.
   * @param idUser        The unique identifier of the user.
   * @param versionNumber The version number of the request.
   * @return The contacts related to the specified request.
   */
  ContactDTO unsupervisedContact(int id, int idUser, int versionNumber);

  /**
   * Retrieves the contacts associated with a specific request.
   *
   * @param id The unique identifier of the request.
   * @return The contacts related to the specified request.
   */
  List<ContactDTO> getEnterpriseContacts(int id);

  /**
   * Retrieves the contacts associated with a specific user.
   *
   * @param id The unique identifier of the user.
   * @return The contacts related to the specified user.
   */
  List<ContactDTO> getUserContacts(int id);
}