package be.vinci.pae.business.contact;

/**
 * This interface represents a Contact in the system. It provides methods for getting and setting
 * the unique identifier, user identifier, enterprise identifier, status, location of the contact
 * meeting, reason for refusing the contact, and academic year associated with the contact. It
 * extends the ContactDTO interface, which provides the basic data transfer object structure for a
 * Contact.
 */
public interface Contact extends ContactDTO {

  /**
   * This method checks if the status change is valid.
   *
   * @param newStatus the new status
   * @param idUser    the id of the user
   * @return true if the status change is valid, false otherwise
   */
  boolean verification(Status newStatus, int idUser);
}