package be.vinci.pae.dal.contact;

import be.vinci.pae.business.contact.ContactDTO;
import java.util.List;

/**
 * This interface provides the methods for the data access logic of the status of a request.
 */
public interface ContactDAO {


  /**
   * Changes the status of a request.
   *
   * @param contactDTO    The contact to update
   * @param versionNumber The version number of the contact
   * @return true if the status change is successful, false otherwise
   */
  boolean changeStatus(ContactDTO contactDTO, int versionNumber);

  /**
   * Changes the status of all contacts for a request.
   *
   * @param contactDTO The contact to update
   * @return true if the status change is successful, false otherwise
   */
  boolean changeAllStatus(ContactDTO contactDTO);

  /**
   * Retrieves a specific contact based on the provided id.
   *
   * @param id the id of the request
   * @return a Contact with the given id. If no contact is found,it returns null.
   */
  ContactDTO getContact(int id);

  /**
   * Returns all the contacts of a request.
   *
   * @param id the id of the request
   * @return a list of contacts for the request
   */
  List<ContactDTO> getContactsByUser(int id);

  /**
   * Returns all the contacts of an enterprise.
   *
   * @param id the id of the enterprise
   * @return a list of contacts for the enterprise
   */
  List<ContactDTO> getContactsByEnterprise(int id);

  /**
   * Retrieves the contact associated with the specified user, enterprise IDs, and school year.
   *
   * @param idUser       The user's ID.
   * @param idEnterprise The enterprise's ID.
   * @param schoolYear   The school year.
   * @return The ContactDTO representing the contact, or null if it doesn't exist.
   */
  ContactDTO getContactByUserAndByEnterprise(int idUser, int idEnterprise, String schoolYear);


  /**
   * Creates a new contact using the provided ContactDTO.
   *
   * @param contact The ContactDTO object containing new contact information.
   * @return The newly created ContactDTO object, or null if creation fails.
   */
  ContactDTO createOne(ContactDTO contact);

  /**
   * Retrieves the accepted contact for the specified user.
   *
   * @param idUser The user's ID.
   * @return The ContactDTO representing the accepted contact, or null if none exists.
   */
  ContactDTO getContactAccepted(int idUser);

}
